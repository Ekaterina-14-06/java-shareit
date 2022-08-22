package ru.practicum.shareit.bookings;

import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.users.User;

import java.util.Set;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    Booking getBookingById(Long id);

    Set<Booking> getAllBookings();

    void removeBookingById(Long id);

    void removeAllBookings();

    Item getItemOfBooking(Long id);

    User getUserOfBooking(Long id);

    Status getStatusOfBooking(Long id);

    Review getReviewOfBooking(Long id);
}
