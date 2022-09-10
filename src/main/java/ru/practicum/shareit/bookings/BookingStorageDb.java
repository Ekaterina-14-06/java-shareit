package ru.practicum.shareit.bookings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class BookingStorageDb implements BookingStorage {
    private final JdbcTemplate jdbcTemplate;

    public BookingStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Booking createBooking(Booking booking) {
        jdbcTemplate.update("INSERT INTO bookings (start, end, item_id, user_id, status_id) VALUES (?, ?, ?, ?, ?)",
                booking.getStart(), booking.getEnd(), booking.getItemId(), booking.getUserId(), booking.getStatusId());
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        jdbcTemplate.update("UPDATE bookings SET start = ?, end = ?, item_id = ?, user_id = ?, status_id = ? WHERE booking_id = ?",
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getUserId(),
                booking.getStatusId(),
                booking.getBookingId());
        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        SqlRowSet bookingRows = jdbcTemplate.queryForRowSet("SELECT * FROM bookings WHERE booking_id = ?", id);
        if (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setBookingId(id);
            booking.setStart(bookingRows.getDate("start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItemId(bookingRows.getLong("item_id"));
            booking.setUserId(bookingRows.getLong("user_id"));
            booking.setStatusId(bookingRows.getLong("status_id"));
            return booking;
        } else {
            return null;
        }
    }

    @Override
    public Set<Booking> getAllBookings() {
        Set<Booking> bookings = new HashSet<>();
        SqlRowSet bookingRows = jdbcTemplate.queryForRowSet("SELECT * FROM bookings");
        while (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setBookingId(bookingRows.getLong("booking_id"));
            booking.setStart(bookingRows.getDate("start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItemId(bookingRows.getLong("item_id"));
            booking.setUserId(bookingRows.getLong("user_id"));
            booking.setStatusId(bookingRows.getLong("status_id"));
            bookings.add(booking);
        }
        return bookings;
    }

    @Override
    public void removeBookingById(Long id) {
        jdbcTemplate.update("DELETE * FROM bookings WHERE booking_id = ?", id);
    }

    @Override
    public void removeAllBookings() {
        jdbcTemplate.update("DELETE * FROM bookings");
    }

    public void changeStatus (Long bookingId, Long statusId) {
        jdbcTemplate.update("UPDATE bookings SET status_id = ? WHERE booking_id = ?", statusId, bookingId);
    }
}
