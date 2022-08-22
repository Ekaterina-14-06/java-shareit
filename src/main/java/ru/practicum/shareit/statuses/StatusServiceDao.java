package ru.practicum.shareit.statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Service
public class StatusServiceDao implements StatusService {
    private final StatusStorageInMemory statusStorageInMemory;
    private final StatusStorageDb statusStorageDb;

    @Autowired
    public StatusServiceDao(StatusStorageInMemory statusStorageInMemory,
                            StatusStorageDb statusStorageDb) {
        this.statusStorageInMemory = statusStorageInMemory;
        this.statusStorageDb = statusStorageDb;
    }

    //=================================================== CRUD =======================================================

    @Override
    public Status createStatus(Status status) {
        Status statusInDb = statusStorageDb.createStatus(status);
        statusStorageInMemory.createStatus(statusInDb);
        return statusInDb;
    }

    @Override
    public Status updateStatus(Status status) {
        statusStorageInMemory.updateStatus(status);
        statusStorageDb.updateStatus(status);
        return status;
    }

    @Override
    public Status getStatusById(Long id) {
        return statusStorageInMemory.getStatusById(id);
    }

    @Override
    public Set<Status> getAllStatuses() {
        return statusStorageInMemory.getAllStatuses();
    }

    @Override
    public void removeStatusById(Long id) {
        statusStorageInMemory.removeStatusById(id);
        statusStorageDb.removeStatusById(id);
    }

    @Override
    public void removeAllStatuses() {
        statusStorageInMemory.removeAllStatuses();
        statusStorageDb.removeAllStatuses();
    }

    //=============================================== БИЗНЕС-ЛОГИКА ===================================================

    @Override
    public Set<Booking> getBookingsOfStatus(Long id) {
        Set<Booking> bookings = new HashSet<>();
        SqlRowSet bookingRows = statusStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM bookings WHERE status = ?", id);
        while (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setId(bookingRows.getLong("id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItem(bookingRows.getLong("item"));
            booking.setBooker(bookingRows.getLong("booker"));
            booking.setStatus(id);
            bookings.add(booking);
        }
        return bookings;
    }

    @Override
    public void removeBookingsOfStatus(Long id) {
        statusStorageDb.getJdbcTemplate().update("DELETE * FROM bookings WHERE status = ?", id);
    }
}