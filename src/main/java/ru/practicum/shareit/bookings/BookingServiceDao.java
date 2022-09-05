package ru.practicum.shareit.bookings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class BookingServiceDao implements BookingService {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final BookingStorageDb bookingStorageDb;
    private final StatusStorageInMemory statusStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;

    @Autowired
    public BookingServiceDao(BookingStorageInMemory bookingStorageInMemory,
                             BookingStorageDb bookingStorageDb,
                             StatusStorageInMemory statusStorageInMemory,
                             ItemStorageInMemory itemStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.bookingStorageDb = bookingStorageDb;
        this.statusStorageInMemory = statusStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
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

    @Override
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
                    log.error("Попытка изменения статуса бронирования не создателем бронирования не владельцем вещи. " +
                            "Операция отменена");
                    throw new ValidationException("Попытка изменения статуса бронирования не создателем бронирования не владельцем вещи. " +
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
        SqlRowSet itemRows = bookingStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM items WHERE id = ?", bookingStorageInMemory.getBookingById(id).getItem());
        if (itemRows.next()) {
            Item item = new Item();
            item.setId(itemRows.getLong("id"));
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequest(itemRows.getLong("request"));
            item.setOwner(id);
            return item;
        }
        return null;
    }

    @Override
    public User getUserOfBooking(Long id) {
        SqlRowSet userRows = bookingStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE id = ?", bookingStorageInMemory.getBookingById(id).getBooker());
        if (userRows.next()) {
            User user = new User();
            user.setId(userRows.getLong("id"));
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            return user;
        }
        return null;
    }

    @Override
    public Status getStatusOfBooking(Long id) {
        SqlRowSet statusRows = bookingStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM statuses WHERE id = ?", bookingStorageInMemory.getBookingById(id).getStatus());
        if (statusRows.next()) {
            Status status = new Status();
            status.setId(statusRows.getLong("id"));
            status.setName(statusRows.getString("name"));
            status.setDescription(statusRows.getString("description"));
            return status;
        }
        return null;
    }

    @Override
    public Review getReviewOfBooking(Long id) {
        SqlRowSet bookingRows = bookingStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM reviews WHERE booking = ?", id);
        if (bookingRows.next()) {
            Review review = new Review();
            review.setId(bookingRows.getLong("id"));
            review.setDescription(bookingRows.getString("description"));
            review.setItem(bookingRows.getLong("item"));
            review.setReviewer(bookingRows.getLong("reviewer"));
            review.setDate(bookingRows.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(bookingRows.getBoolean("evaluation"));
            review.setBooking(bookingRows.getLong("booking"));
            return review;
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
