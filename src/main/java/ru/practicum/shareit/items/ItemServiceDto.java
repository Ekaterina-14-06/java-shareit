package ru.practicum.shareit.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingDto;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.requests.ItemRequestStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewDto;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;
import ru.practicum.shareit.users.UserServiceDto;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class ItemServiceDto {
    private final ItemStorageInMemory itemStorageInMemory;
    private final UserStorageInMemory userStorageInMemory;
    private final ItemServiceImpl itemServiceImpl;
    private final UserServiceDto userServiceDto;
    private final ReviewStorageInMemory reviewStorageInMemory;
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;

    @Autowired
    public ItemServiceDto(ItemStorageInMemory itemStorageInMemory,
                          UserStorageInMemory userStorageInMemory,
                          ItemServiceImpl itemServiceImpl,
                          UserServiceDto userServiceDto,
                          ReviewStorageInMemory reviewStorageInMemory,
                          ItemRequestStorageInMemory itemRequestStorageInMemory,
                          BookingStorageInMemory bookingStorageInMemory) {
        this.itemStorageInMemory = itemStorageInMemory;
        this.userStorageInMemory = userStorageInMemory;
        this.itemServiceImpl = itemServiceImpl;
        this.userServiceDto = userServiceDto;
        this.reviewStorageInMemory = reviewStorageInMemory;
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
    }

    public ItemDto getItemById(Long id) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(itemStorageInMemory.getItemById(id).getName());
        itemDto.setDescription(itemStorageInMemory.getItemById(id).getDescription());
        itemDto.setAvailable(itemStorageInMemory.getItemById(id).getAvailable());
        return itemDto;
    }

    public UserDto getUserOfItem(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == itemServiceImpl.getItemById(id).getUserId()) {
                return userServiceDto.getUserById(id);
            }
        }
        return null;
    }

    public Set<ReviewDto> getReviewsOfItem(Long id) {
        Set<ReviewDto> reviewsOfItem = new HashSet<>();
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getItemId() == id) {
                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setDescription(review.getDescription());
                reviewDto.setItemId(review.getItemId());
                reviewDto.setDate(review.getDate());
                reviewDto.setEvaluation(review.getEvaluation());
                reviewDto.setBookingId(review.getBookingId());
                reviewsOfItem.add(reviewDto);
            }
        }
        return reviewsOfItem;
    }

    public Set<BookingDto> getBookingsOfItem(Long id) {
        Set<BookingDto> bookingsOfItem = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getItemId() == id) {
                BookingDto bookingDto = new BookingDto();
                bookingDto.setStart(booking.getStart());
                bookingDto.setEnd(booking.getEnd());
                bookingDto.setItemId(booking.getItemId());
                bookingDto.setStatusId(booking.getStatusId());
                bookingsOfItem.add(bookingDto);
            }
        }
        return bookingsOfItem;
    }

    public Set<ItemDto> findItemById(String text) {
        Set<ItemDto> items = new HashSet<>();
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                ItemDto itemDto = new ItemDto();
                itemDto.setName(item.getName());
                itemDto.setDescription(item.getDescription());
                itemDto.setAvailable(item.getAvailable());
                items.add(itemDto);
            }
        }
        return items;
    }
}
