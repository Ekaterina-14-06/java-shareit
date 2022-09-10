package ru.practicum.shareit.bookings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final BookingStorageDb bookingStorageDb;
    private final ItemStorageInMemory itemStorageInMemory;
    private final UserStorageInMemory userStorageInMemory;
    private final StatusStorageInMemory statusStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;

    @Autowired
    public BookingServiceImpl(BookingStorageInMemory bookingStorageInMemory,
                              BookingStorageDb bookingStorageDb,
                              ItemStorageInMemory itemStorageInMemory,
                              UserStorageInMemory userStorageInMemory,
                              StatusStorageInMemory statusStorageInMemory,
                              ReviewStorageInMemory reviewStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.bookingStorageDb = bookingStorageDb;
        this.itemStorageInMemory = itemStorageInMemory;
        this.userStorageInMemory = userStorageInMemory;
        this.statusStorageInMemory = statusStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
    }

    @Override
    public Booking createBooking(Booking booking, Long userId) {
        booking.setUserId(userId);
        booking.setStatusId(statusStorageInMemory.getStatusIdByName("waiting"));
        Booking bookingInDb = bookingStorageDb.createBooking(booking);
        bookingStorageInMemory.createBooking(bookingInDb);
        return bookingInDb;
    }

    @Override
    public Booking updateBooking(Booking booking, Long userId) {
        if (booking.getUserId() == userId) {
            bookingStorageInMemory.updateBooking(booking);
            bookingStorageDb.updateBooking(booking);
        }
        return booking;
    }

    public void changeStatus (Long bookingId, Boolean approved, Long statusId, Long userId) {
        try {
            if (itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(bookingId).getItemId()).getUserId() == userId) {
                if (!(bookingStorageInMemory.getBookingById(bookingId).getStatusId() ==
                        statusStorageInMemory.getStatusIdByName("canceled"))) {
                    if (approved) {
                        statusId = statusStorageInMemory.getStatusIdByName("approved");
                    } else {
                        statusId = statusStorageInMemory.getStatusIdByName("rejected");
                    }
                    bookingStorageInMemory.changeStatus(bookingId, statusId);
                    bookingStorageDb.changeStatus(bookingId, statusId);
                }
            } else {
                if (bookingStorageInMemory.getBookingById(bookingId).getUserId() == userId) {
                    if ((bookingStorageInMemory.getBookingById(bookingId).getStatusId() ==
                            statusStorageInMemory.getStatusIdByName("canceled"))) {
                        statusId = statusStorageInMemory.getStatusIdByName("canceled");
                        bookingStorageInMemory.changeStatus(bookingId, statusId);
                        bookingStorageDb.changeStatus(bookingId, statusId);
                    }
                } else {
                    log.error("Попытка изменения статуса бронирования не создателем бронирования и не владельцем вещи. " +
                            "Операция отменена");
                    throw new ValidationException("Попытка изменения статуса бронирования не создателем бронирования и не владельцем вещи. " +
                            "Операция отменена");
                }
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<Booking> getBookingById(Long id, Long userId) {
        if (bookingStorageInMemory.getBookingById(id).getUserId() == userId ||
                itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(id).getItemId()).getUserId() == userId) {
            return Optional.of(bookingStorageInMemory.getBookingById(id));
        }
        return Optional.empty();
    }

    @Override
    public Set<Booking> getAllBookings() {
        return bookingStorageInMemory.getAllBookings();
    }

    @Override
    public void removeBookingById(Long id) {
        bookingStorageInMemory.removeBookingById(id);
        bookingStorageDb.removeBookingById(id);
    }

    @Override
    public void removeAllBookings() {
        bookingStorageInMemory.removeAllBookings();
        bookingStorageDb.removeAllBookings();
    }

    @Override
    public Optional<Item> getItemOfBooking(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getItemId() == bookingStorageInMemory.getBookingById(id).getItemId()) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserOfBooking(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == bookingStorageInMemory.getBookingById(id).getUserId()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Status> getStatusOfBooking(Long id) {
        for (Status status : statusStorageInMemory.getAllStatuses()) {
            if (status.getStatusId() == bookingStorageInMemory.getBookingById(id).getStatusId()) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Review> getReviewOfBooking(Long id) {
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getBookingId() == id) {
                return Optional.of(review);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<Booking> getBookingsOfUser(String state, Long userId) {
        Set<Booking> bookingsOfUser = new HashSet<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getUserId() == userId) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
        }
        return bookingsOfUser;
    }

    @Override
    public Set<Booking> getBookingsOfOwner(String state, Long userId) {
        Set<Booking> bookingsOfUser = new HashSet<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId &&
                        booking.getStatusId() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItemId()).getUserId() == userId) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
        }
        return bookingsOfUser;
    }
}
