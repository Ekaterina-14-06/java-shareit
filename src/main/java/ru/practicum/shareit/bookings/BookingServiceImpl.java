package ru.practicum.shareit.bookings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.HashSet;
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
        booking.setBooker(userId);
        booking.setStatus(statusStorageInMemory.getStatusIdByName("waiting"));
        Booking bookingInDb = bookingStorageDb.createBooking(booking);
        bookingStorageInMemory.createBooking(bookingInDb);
        return bookingInDb;
    }

    @Override
    public Booking updateBooking(Booking booking, Long userId) {
        if (booking.getBooker() == userId) {
            bookingStorageInMemory.updateBooking(booking);
            bookingStorageDb.updateBooking(booking);
        }
        return booking;
    }

    public void changeStatus (Long bookingId, Boolean approved, Long statusId, Long userId) {
        try {
            if (itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(bookingId).getItem()).getOwner() == userId) {
                if (!(bookingStorageInMemory.getBookingById(bookingId).getStatus() ==
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
                if (bookingStorageInMemory.getBookingById(bookingId).getBooker() == userId) {
                    if ((bookingStorageInMemory.getBookingById(bookingId).getStatus() ==
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
    public Booking getBookingById(Long id, Long userId) {
        if (bookingStorageInMemory.getBookingById(id).getBooker() == userId ||
                itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(id).getItem()).getOwner() == userId) {
            return bookingStorageInMemory.getBookingById(id);
        }
        return null;
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
    public Item getItemOfBooking(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getId() == bookingStorageInMemory.getBookingById(id).getItem()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public User getUserOfBooking(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getId() == bookingStorageInMemory.getBookingById(id).getBooker()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Status getStatusOfBooking(Long id) {
        for (Status status : statusStorageInMemory.getAllStatuses()) {
            if (status.getId() == bookingStorageInMemory.getBookingById(id).getStatus()) {
                return status;
            }
        }
        return null;
    }

    @Override
    public Review getReviewOfBooking(Long id) {
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getBooking() == id) {
                return review;
            }
        }
        return null;
    }

    @Override
    public Set<Booking> getBookingsOfUser(String state, Long userId) {
        Set<Booking> bookingsOfUser = new HashSet<>();
        switch (state) {
            case ("CURRENT"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getBooker() == userId) {
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
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isBefore(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("PAST"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getEnd().isBefore(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("FUTURE"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                        booking.getStart().isAfter(LocalDateTime.now()) &&
                        booking.getEnd().isAfter(LocalDateTime.now())) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("WAITING"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("waiting")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            case ("REJECTED"):
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId &&
                        booking.getStatus() == statusStorageInMemory.getStatusIdByName("rejected")) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
            default: // "ALL"
                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (itemStorageInMemory.getItemById(booking.getItem()).getOwner() == userId) {
                        bookingsOfUser.add(booking);
                    }
                }
                break;
        }
        return bookingsOfUser;
    }
}
