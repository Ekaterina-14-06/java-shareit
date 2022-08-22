package ru.practicum.shareit.bookings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashSet;
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
        jdbcTemplate.update("INSERT INTO bookings (start, end, item, booker, status) VALUES (?, ?, ?, ?, ?)",
                booking.getStart(), booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        jdbcTemplate.update("UPDATE bookings SET start = ?, end = ?, item = ?, booker = ?, status = ? WHERE id = ?",
                booking.getStart(), booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus(), booking.getId());
        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        SqlRowSet bookingRows = jdbcTemplate.queryForRowSet("SELECT * FROM bookings WHERE id = ?", id);
        if (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setId(id);
            booking.setStart(bookingRows.getDate("start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItem(bookingRows.getLong("item"));
            booking.setBooker(bookingRows.getLong("booker"));
            booking.setStatus(bookingRows.getLong("status"));
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
            booking.setId(bookingRows.getLong("id"));
            booking.setStart(bookingRows.getDate("start").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItem(bookingRows.getLong("item"));
            booking.setBooker(bookingRows.getLong("booker"));
            booking.setStatus(bookingRows.getLong("status"));
            bookings.add(booking);
        }
        return bookings;
    }

    @Override
    public void removeBookingById(Long id) {
        jdbcTemplate.update("DELETE * FROM bookings WHERE id = ?", id);
    }

    @Override
    public void removeAllBookings() {
        jdbcTemplate.update("DELETE * FROM bookings");
    }
}
