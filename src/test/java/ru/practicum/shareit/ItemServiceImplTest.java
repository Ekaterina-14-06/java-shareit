package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    private ItemService itemService;
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;
    private UserRepository userRepository;
    private ItemRequestRepository itemRequestRepository;
    private BookingRepository bookingRepository;
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemMapper = new ItemMapper();
        BookingMapper bookingMapper = new BookingMapper();
        reviewMapper = new ReviewMapper();
        itemService = new ItemServiceImpl(itemRepository, itemMapper,
                userRepository, bookingRepository, bookingMapper,
                reviewRepository, reviewMapper, itemRequestRepository);
    }

    private Item createItem() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(2L, "user2", "user2@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "itemRequest1",
                user2, LocalDateTime.now());
        return new Item(1L, "item1", "description1",
                true, user1, itemRequest);
    }

    @Test
    void findItemByIdTest() {
        Item item = createItem();
        Long itemId = item.getId();
        long incorrectId = (long) (Math.random() * 100) + itemId + 3;
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        when(itemRepository.findById(incorrectId))
                .thenThrow(new BookingException("Вещи с Id = " + incorrectId + " нет в БД"));
        ItemDtoWithBooking itemDtoWithBooking = itemService.findById(itemId, item.getOwner().getId());
        assertNotNull(itemDtoWithBooking);
        assertEquals("item1", itemDtoWithBooking.getName());
        Throwable thrown = assertThrows(BookingException.class,
                () -> itemService.findById(incorrectId, item.getOwner().getId()));
        assertNotNull(thrown.getMessage());
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void findAllItemsTest() {
        Item item = createItem();
        User userWriteReview = item.getItemRequest().getRequestor();
        Review review = createReview(item, userWriteReview);
        when(reviewRepository.findAllByItemId(item.getId()))
                .thenReturn(Collections.singletonList(review));
        when(itemRepository.findByOwnerId(item.getOwner().getId(), PageRequest.of(0, 20)))
                .thenReturn(Collections.singletonList(item));
        final List<ItemDtoWithBooking> items = itemService
                .findAll(item.getOwner().getId(), 0, 20);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getName(), items.get(0).getName());
        verify(itemRepository, times(1))
                .findByOwnerId(item.getOwner().getId(), PageRequest.of(0, 20));
    }

    @Test
    void saveItemTest() {
        Item item = createItem();
        when(itemRepository.save(item))
                .thenReturn(item);
        when(userRepository.findById(item.getOwner().getId()))
                .thenReturn(Optional.of(item.getOwner()));
        when(itemRequestRepository.findById(item.getItemRequest().getId()))
                .thenReturn(Optional.of(item.getItemRequest()));
        ItemDto itemDto = itemService.save(itemMapper.toItemDto(item), item.getOwner().getId());
        assertNotNull(itemDto);
        assertEquals("item1", itemDto.getName());
        assertEquals("description1", itemDto.getDescription());
        assertEquals(item.getId(), itemDto.getId());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void saveReviewForItemTest() {
        Item item = createItem();
        User userWriteReview = item.getItemRequest().getRequestor();
        Review review = createReview(item, userWriteReview);
        Booking booking = createBooking(item, userWriteReview);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(userWriteReview.getId())).thenReturn(Optional.of(userWriteReview));
        List<Booking> bookingsList = new ArrayList<>();
        bookingsList.add(booking);
        when(bookingRepository
                .searchBookingByBookerIdAndItemIdAndEndIsBeforeAndStatus(anyLong(), anyLong(), any(), any()))
                .thenReturn(bookingsList);
        when(reviewRepository.save(review))
                .thenReturn(review);
        ReviewDto reviewDto1 = reviewMapper.toReviewDto(review);
        ReviewDto reviewDto = itemService.saveReview(userWriteReview.getId(), item.getId(),
                reviewDto1);
        assertNotNull(reviewDto);
        assertEquals("Great", reviewDto.getText());
        assertEquals(userWriteReview.getName(), reviewDto.getAuthorName());
        assertEquals(review.getId(), reviewDto.getId());
        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void exceptionIncorrectUserIdTest() {
        Item item = createItem();
        User userWriteReview = item.getItemRequest().getRequestor();
        Review review = createReview(item, userWriteReview);
        ReviewDto reviewDto = reviewMapper.toReviewDto(review);
        long incorrectUserId = 10L;
        Throwable thrown = assertThrows(BookingException.class,
                () -> itemService.saveReview(incorrectUserId,
                        item.getOwner().getId(), reviewDto));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void exceptionIncorrectItemIdTest() {
        Item item = createItem();
        User userWriteReview = item.getItemRequest().getRequestor();
        Review review = createReview(item, userWriteReview);
        ReviewDto reviewDto = reviewMapper.toReviewDto(review);
        long incorrectItemId = 10L;
        Throwable thrown2 = assertThrows(BookingException.class,
                () -> itemService.saveReview(userWriteReview.getId(),
                        incorrectItemId, reviewDto));
        assertNotNull(thrown2.getMessage());
    }

    private Review createReview(Item item, User user) {
        return new Review(1L, "Great", item, user, LocalDateTime.now());
    }

    private Booking createBooking(Item item, User userWriteReview) {
        return new Booking(1L, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2),
                item, userWriteReview, Status.ACCEPTED);
    }

    @Test
    void updateItemTest() {
        Item item = createItem();
        Item item2 = createItem();
        long itemId = item.getId();
        item2.setName("item2");
        when(itemRepository.save(any(Item.class))).thenReturn(item2);
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        ItemDto itemDto = itemService.update(itemMapper.toItemDto(item2),
                item.getOwner().getId(), itemId);
        assertNotNull(itemDto);
        assertEquals("item2", itemDto.getName());
        assertEquals("description1", itemDto.getDescription());
        assertEquals(item.getId(), itemDto.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void deleteItemByIdTest() {
        Item item = createItem();
        itemService.deleteById(item.getId());
        verify(itemRepository, times(1)).deleteById(item.getId());
    }

    @Test
    void searchItemByTextTest() {
        List<Item> items = new ArrayList<>();
        Item item = createItem();
        items.add(item);
        String text = item.getDescription().substring(0, 3);
        when(itemRepository.search(text, PageRequest.of(0, 20))).thenReturn(items);
        List<ItemDto> itemDtos = itemService.searchItem(text, 0, 20);
        assertNotNull(itemDtos);
        assertEquals(1, itemDtos.size());
        assertEquals(item.getName(), itemDtos.get(0).getName());
        verify(itemRepository, times(1))
                .search(text, PageRequest.of(0, 20));
    }
}