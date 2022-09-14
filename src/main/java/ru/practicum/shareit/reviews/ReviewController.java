package ru.practicum.shareit.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.BookingDto;


import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewServiceImpl;
    private final ReviewServiceDto reviewServiceDto;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewServiceImpl,
                            ReviewServiceDto reviewServiceDto) {
        this.reviewServiceImpl = reviewServiceImpl;
        this.reviewServiceDto = reviewServiceDto;
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
    public ReviewDto getReviewById(@PathVariable("id") Long id) {
        return reviewServiceDto.getReviewById(id);
    }

    @GetMapping()
    public Set<ReviewDto> getAllReviews() {
        return reviewServiceDto.getAllReviews();
    }

    @GetMapping("/{id}/bookings")
    public BookingDto getBookingOfReview(@PathVariable("id") Long id) {
        return reviewServiceDto.getBookingOfReview(id);
    }
}