package ru.practicum.shareit.statuses;

import ru.practicum.shareit.bookings.Booking;

import java.util.Set;

public interface StatusService {
    Status createStatus(Status status);

    Status updateStatus(Status status);

    Status getStatusById(Long id);

    Set<Status> getAllStatuses();

    void removeStatusById(Long id);

    void removeAllStatuses();

    Set<Booking> getBookingsOfStatus(Long id);

    void removeBookingsOfStatus(Long id);
}
