package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.users.User;

import java.time.ZoneId;
import java.util.Set;

@Service
public class BookingServiceDao implements BookingService {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final BookingStorageDb bookingStorageDb;

    @Autowired
    public BookingServiceDao(BookingStorageInMemory bookingStorageInMemory, BookingStorageDb bookingStorageDb) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.bookingStorageDb = bookingStorageDb;
    }

    @Override
    public Booking createBooking(Booking booking) {
        Booking bookingInDb = bookingStorageDb.createBooking(booking);
        bookingStorageInMemory.createBooking(bookingInDb);
        return bookingInDb;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        bookingStorageInMemory.updateBooking(booking);
        bookingStorageDb.updateBooking(booking);
        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingStorageInMemory.getBookingById(id);
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
                "SELECT * FROM items WHERE id = ?", getBookingById(id).getItem());
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
                "SELECT * FROM users WHERE id = ?", getBookingById(id).getBooker());
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
                "SELECT * FROM statuses WHERE id = ?", getBookingById(id).getStatus());
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
}
