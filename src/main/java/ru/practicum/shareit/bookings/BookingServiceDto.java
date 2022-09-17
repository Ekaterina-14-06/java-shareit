package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemDto;
import ru.practicum.shareit.items.ItemServiceDto;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewDto;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusDto;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BookingServiceDto {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;
    private final StatusStorageInMemory statusStorageInMemory;
    private final ItemServiceDto itemServiceDto;
    private final UserStorageInMemory userStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;

    @Autowired
    public BookingServiceDto(BookingStorageInMemory bookingStorageInMemory,
                             ItemStorageInMemory itemStorageInMemory,
                             StatusStorageInMemory statusStorageInMemory,
                             ItemServiceDto itemServiceDto,
                             UserStorageInMemory userStorageInMemory,
                             ReviewStorageInMemory reviewStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
        this.statusStorageInMemory = statusStorageInMemory;
        this.itemServiceDto = itemServiceDto;
        this.userStorageInMemory = userStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
    }

    public BookingDto getBookingById(Long id, Long userId) {
        if (bookingStorageInMemory.getBookingById(id).getUserId() == userId ||
                itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(id).getItemId()).getUserId() == userId) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setStart(bookingStorageInMemory.getBookingById(id).getStart());
            bookingDto.setEnd(bookingStorageInMemory.getBookingById(id).getEnd());
            bookingDto.setItemId(bookingStorageInMemory.getBookingById(id).getItemId());
            bookingDto.setStatusId(bookingStorageInMemory.getBookingById(id).getStatusId());
            return bookingDto;
        }
        return null;
    }

    public Set<BookingDto> getAllBookingDtos() {
        return bookingStorageInMemory.getAllBookingDtos();
    }

    public Set<BookingDto> getBookingsOfUser(String state, Long userId) {
        Set<BookingDto> bookingsOfUser = new HashSet<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(booking.getItemId());
                        bookingDto.setStatusId(booking.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
        }
        return bookingsOfUser;
    }

    public Set<BookingDto> getBookingsOfOwner(String state, Long userId) {
        Set<BookingDto> bookingsOfUser = new HashSet<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId) {
                        BookingDto bookingDto = new BookingDto();
                        bookingDto.setStart(booking.getStart());
                        bookingDto.setEnd(booking.getEnd());
                        bookingDto.setItemId(bookingDto.getItemId());
                        bookingDto.setStatusId(bookingDto.getStatusId());
                        bookingsOfUser.add(bookingDto);
                    }
                }
                break;
        }
        return bookingsOfUser;
    }

    public Optional<ItemDto> getItemOfBooking(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getItemId() == bookingStorageInMemory.getBookingById(id).getItemId()) {
                return Optional.of(itemServiceDto.getItemById(item.getItemId()));
            }
        }
        return Optional.empty();
    }

    public Optional<UserDto> getUserOfBooking(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == bookingStorageInMemory.getBookingById(id).getUserId()) {
                UserDto userDto = new UserDto();
                userDto.setName(user.getName());
                userDto.setEmail(user.getEmail());
                return Optional.of(userDto);
            }
        }
        return Optional.empty();
    }

    public Optional<StatusDto> getStatusOfBooking(Long id) {
        for (Status status : statusStorageInMemory.getAllStatuses()) {
            if (status.getStatusId() == bookingStorageInMemory.getBookingById(id).getStatusId()) {
                StatusDto statusDto = new StatusDto();
                statusDto.setName(status.getName());
                statusDto.setDescription(status.getDescription());
                return Optional.of(statusDto);
            }
        }
        return Optional.empty();
    }

    public Optional<ReviewDto> getReviewOfBooking(Long id) {
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getBookingId() == id) {
                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setDescription(review.getDescription());
                reviewDto.setItemId(review.getItemId());
                reviewDto.setDate(review.getDate());
                reviewDto.setEvaluation(review.getEvaluation());
                reviewDto.setBookingId(review.getBookingId());
                return Optional.of(reviewDto);
            }
        }
        return Optional.empty();
    }
}
