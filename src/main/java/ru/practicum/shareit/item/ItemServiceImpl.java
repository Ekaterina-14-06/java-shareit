package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingDtoForItem;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ItemException;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper,
                           UserRepository userRepository, BookingRepository bookingRepository,
                           BookingMapper bookingMapper, ReviewRepository reviewRepository,
                           ReviewMapper reviewMapper, ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public ItemDtoWithBooking findById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException("Вещи с Id = " + itemId + " нет в БД"));
        ItemDtoWithBooking itemDtoWithBooking = itemMapper
                .toItemDtoWithBooking(item);
        if (item.getOwner().getId() == userId) {
            createItemDtoWithBooking(itemDtoWithBooking);
        }
        List<Review> reviews = reviewRepository.findAllByItemId(itemId);
        if (!reviews.isEmpty()) {
            itemDtoWithBooking.setReviews(reviews.stream().map(reviewMapper::toReviewDto)
                    .collect(Collectors.toList()));
        }
        return itemDtoWithBooking;
    }

    @Override
    public List<ItemDtoWithBooking> findAll(long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<ItemDtoWithBooking> result = itemRepository.findByOwnerId(userId, pageable).stream()
                .map(itemMapper::toItemDtoWithBooking)
                .collect(Collectors.toList());
        for (ItemDtoWithBooking itemDtoWithBooking : result) {
            createItemDtoWithBooking(itemDtoWithBooking);
            List<Review> reviews = reviewRepository.findAllByItemId(itemDtoWithBooking.getId());
            if (!reviews.isEmpty()) {
                itemDtoWithBooking.setReviews(reviews
                        .stream().map(reviewMapper::toReviewDto)
                        .collect(Collectors.toList()));
            }
        }
        result.sort(Comparator.comparing(ItemDtoWithBooking::getId));
        return result;
    }

    private void createItemDtoWithBooking(ItemDtoWithBooking itemDtoWithBooking) {
        List<Booking> lastBookings = bookingRepository
                .findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            BookingDtoForItem lastBooking = bookingMapper.toBookingDtoForItem(lastBookings.get(0));
            itemDtoWithBooking.setLastBooking(lastBooking);
        }
        List<Booking> nextBookings = bookingRepository
                .findBookingsByItemIdAndStartIsAfterOrderByStartDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            BookingDtoForItem nextBooking = bookingMapper.toBookingDtoForItem(nextBookings.get(0));
            itemDtoWithBooking.setNextBooking(nextBooking);
        }
    }

    @Override
    public ItemDto save(ItemDto itemDto, long userId) {
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ItemException("Incorrect userId")));
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            item.setItemRequest(itemRequestRepository.findById(requestId)
                    .orElseThrow(() -> new ItemException("Incorrect RequestId")));
        }
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ReviewDto saveReview(long userId, long itemId, ReviewDto reviewDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemException("Item exception: " + itemId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ItemException(""));
        List<Booking> bookings = bookingRepository
                .searchBookingByBookerIdAndItemIdAndEndIsBeforeAndStatus(userId, itemId,
                        LocalDateTime.now(), Status.ACCEPTED);
        if (bookings.isEmpty()) {
            throw new BookingException("Booking Exception with Item Id " + itemId);
        }
        Review review = reviewMapper.toReview(reviewDto);
        review.setItem(item);
        review.setAuthor(user);
        reviewRepository.save(review);
        return reviewMapper.toReviewDto(review);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId, long id) {
        try {
            Item oldItem = itemRepository.findById(id).orElseThrow();

            if (oldItem.getOwner().getId() == userId) {

                if (itemDto.getName() != null) {
                    oldItem.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null) {
                    oldItem.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    oldItem.setAvailable(itemDto.getAvailable());
                }
                return itemMapper.toItemDto(itemRepository.save(oldItem));
            } else {
                throw new BookingException("Incorrect userId");
            }
        } catch (Exception e) {
            throw new BookingException("Incorrect ItemId");
        }
    }

    @Override
    public void deleteById(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItem(String text, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        if (!text.isBlank()) {
            return itemRepository.search(text, pageable)
                    .stream()
                    .filter(Item::getAvailable)
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
