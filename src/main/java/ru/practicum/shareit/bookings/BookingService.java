package ru.practicum.shareit.bookings;

import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.users.User;

import java.util.Optional;
import java.util.Set;

public interface BookingService {
    Booking createBooking(Booking booking, Long userId);

    Booking updateBooking(Booking booking, Long userId);

    void changeStatus (Long bookingId, Boolean approved, Long statusId, Long userId);

    Optional<Booking> getBookingById(Long id, Long userId);

    Set<Booking> getAllBookings();

    void removeBookingById(Long id);

    void removeAllBookings();

    Optional<Item> getItemOfBooking(Long id);

    Optional<User> getUserOfBooking(Long id);

    Optional<Status> getStatusOfBooking(Long id);

    Optional<Review> getReviewOfBooking(Long id);

    Set<Booking> getBookingsOfUser(String state, Long userId);

    Set<Booking> getBookingsOfOwner(String state, Long userId);
}
