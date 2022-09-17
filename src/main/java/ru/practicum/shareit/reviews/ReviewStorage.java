package ru.practicum.shareit.reviews;

import java.util.Set;

public interface ReviewStorage {
    Review createReview(Review review);

    Review updateReview(Review review);

    Review getReviewById(Long id);

    Set<Review> getAllReviews();

    void removeReviewById(Long id);

    void removeAllReviews();
}
