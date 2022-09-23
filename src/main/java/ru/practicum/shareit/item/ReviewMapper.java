package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public ReviewDto toReviewDto(Review review) {
        return new ReviewDto(review.getId(),
                review.getText(),
                review.getAuthor().getName(),
                review.getCreated()
        );
    }

    public Review toReview(ReviewDto reviewDto) {
        return new Review(reviewDto.getId(),
                reviewDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }
}
