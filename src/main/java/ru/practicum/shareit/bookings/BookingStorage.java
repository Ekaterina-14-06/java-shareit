package ru.practicum.shareit.bookings;

import java.util.Set;

public interface BookingStorage {
    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    Booking getBookingById(Long id);

    Set<Booking> getAllBookings();

    void removeBookingById(Long id);

    void removeAllBookings();
}
