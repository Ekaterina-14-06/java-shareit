package ru.practicum.shareit.statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusStorageInMemory statusStorageInMemory;
    private final StatusStorageDb statusStorageDb;
    private final BookingStorageInMemory bookingStorageInMemory;

    @Autowired
    public StatusServiceImpl(StatusStorageInMemory statusStorageInMemory,
                             StatusStorageDb statusStorageDb,
                             BookingStorageInMemory bookingStorageInMemory) {
        this.statusStorageInMemory = statusStorageInMemory;
        this.statusStorageDb = statusStorageDb;
        this.bookingStorageInMemory = bookingStorageInMemory;
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
        Set<Booking> bookingsOfStatus = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getStatus() == id) {
                bookingsOfStatus.add(booking);
            }
        }
        return bookingsOfStatus;
    }

    @Override
    public void removeBookingsOfStatus(Long id) {
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getStatus() == id) {
                bookingStorageInMemory.getAllBookings().remove(booking);
            }
        }
    }
}
