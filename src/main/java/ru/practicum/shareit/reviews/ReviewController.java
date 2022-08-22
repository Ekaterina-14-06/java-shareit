package ru.practicum.shareit.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.Booking;


import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewServiceImpl;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewServiceImpl) {
        this.reviewServiceImpl = reviewServiceImpl;
    }

    @PostMapping()
    public void addReview(@Valid @RequestBody Review review) {
        reviewServiceImpl.createReview(review);
    }

    @PutMapping()
    public void updateReview(@Valid @RequestBody Review review) {
        reviewServiceImpl.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void removeReviewById(@PathVariable("id") Long id) {
        reviewServiceImpl.removeReviewById(id);
    }

    @DeleteMapping()
    public void removeAllReviews() {
        reviewServiceImpl.removeAllReviews();
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable("id") Long id) {
        return reviewServiceImpl.getReviewById(id);
    }

    @GetMapping()
    public Set<Review> getAllReviews() {
        return reviewServiceImpl.getAllReviews();
    }

    @GetMapping("/{id}/bookings")
    public Booking getBookingOfReview(@PathVariable("id") Long id) {
        return reviewServiceImpl.getBookingOfReview(id);
    }
}