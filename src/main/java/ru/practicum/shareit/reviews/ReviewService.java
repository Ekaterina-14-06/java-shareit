package ru.practicum.shareit.reviews;

import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.users.User;

import java.util.Set;

public interface ReviewService {
    Review createReview(Review review);

    Review updateReview(Review review);

    Review getReviewById(Long id);

    Set<Review> getAllReviews();

    void removeReviewById(Long id);

    void removeAllReviews();

    User getUserOfReview(Long id);

    Item getItemOfReview(Long id);

    Booking getBookingOfReview(Long id);
}
