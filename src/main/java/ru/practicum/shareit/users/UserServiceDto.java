package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingDto;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemDto;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestDto;
import ru.practicum.shareit.requests.ItemRequestStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewDto;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceDto {
    private final UserStorageInMemory userStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;

    @Autowired
    public UserServiceDto(UserStorageInMemory userStorageInMemory,
                          ItemStorageInMemory itemStorageInMemory,
                          ItemRequestStorageInMemory itemRequestStorageInMemory,
                          BookingStorageInMemory bookingStorageInMemory,
                          ReviewStorageInMemory reviewStorageInMemory) {
        this.userStorageInMemory = userStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
    }

    public UserDto getUserById(Long id) {
        UserDto userDto = new UserDto();
        userDto.setName(userStorageInMemory.getUserById(id).getName());
        userDto.setEmail(userStorageInMemory.getUserById(id).getEmail());
        return userDto;
    }

    public Set<UserDto> getAllUsers() {
        Set<UserDto> userDtos = new HashSet<>();
        for (User user : userStorageInMemory.getAllUsers()) {
            UserDto userDto = new UserDto();
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDtos.add(userDto);
        }
        return userDtos;
    }

    public Set<ItemDto> getItemsOfUser(Long id) {
        Set<ItemDto> itemsOfUser = new HashSet<>();
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getUserId() == id) {
                ItemDto itemDto = new ItemDto();
                itemDto.setName(item.getName());
                itemDto.setDescription(item.getDescription());
                itemDto.setAvailable(item.getAvailable());
                itemsOfUser.add(itemDto);
            }
        }
        return itemsOfUser;
    }

    public Set<ItemRequestDto> getItemRequestsOfUser(Long id) {
        Set<ItemRequestDto> itemRequestsOfUser = new HashSet<>();
        for (ItemRequest itemRequest : itemRequestStorageInMemory.getAllItemRequests()) {
            if (itemRequest.getUserId() == id) {
                ItemRequestDto itemRequestDto = new ItemRequestDto();
                itemRequestDto.setDescription(itemRequest.getDescription());
                itemRequestDto.setCreated(itemRequest.getCreated());
                itemRequestsOfUser.add(itemRequestDto);
            }
        }
        return itemRequestsOfUser;
    }

    public Set<BookingDto> getBookingsOfUser(Long id) {
        Set<BookingDto> bookingsOfUser = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getUserId() == id) {
                BookingDto bookingDto = new BookingDto();
                bookingDto.setStart(booking.getStart());
                bookingDto.setEnd(booking.getEnd());
                bookingDto.setItemId(booking.getItemId());
                bookingDto.setStatusId(booking.getStatusId());
                bookingsOfUser.add(bookingDto);
            }
        }
        return bookingsOfUser;
    }

    public Set<ReviewDto> getReviewsOfUser(Long id) {
        Set<ReviewDto> reviewsOfUser = new HashSet<>();
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getUserId() == id) {
                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setDescription(review.getDescription());
                reviewDto.setItemId(review.getItemId());
                reviewDto.setDate(review.getDate());
                reviewDto.setEvaluation(review.getEvaluation());
                reviewDto.setBookingId(review.getBookingId());
                reviewsOfUser.add(reviewDto);
            }
        }
        return reviewsOfUser;
    }
}