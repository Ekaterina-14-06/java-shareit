package ru.practicum.shareit.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingDto;
import ru.practicum.shareit.bookings.BookingStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class ReviewServiceDto {
    private final ReviewStorageInMemory reviewStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;

    @Autowired
    public ReviewServiceDto(ReviewStorageInMemory reviewStorageInMemory,
                            BookingStorageInMemory bookingStorageInMemory) {
        this.reviewStorageInMemory = reviewStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
    }

    public ReviewDto getReviewById(Long id) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setDescription(reviewStorageInMemory.getReviewById(id).getDescription());
        reviewDto.setItemId(reviewStorageInMemory.getReviewById(id).getItemId());
        reviewDto.setDate(reviewStorageInMemory.getReviewById(id).getDate());
        reviewDto.setEvaluation(reviewStorageInMemory.getReviewById(id).getEvaluation());
        reviewDto.setBookingId(reviewStorageInMemory.getReviewById(id).getBookingId());
        return reviewDto;
    }

    public Set<ReviewDto> getAllReviews() {
        Set<ReviewDto> reviewDtos = new HashSet<>();
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setDescription(review.getDescription());
            reviewDto.setItemId(review.getItemId());
            reviewDto.setDate(review.getDate());
            reviewDto.setEvaluation(review.getEvaluation());
            reviewDto.setBookingId(review.getBookingId());
            reviewDtos.add(reviewDto);
        }
        return reviewDtos;
    }

    public BookingDto getBookingOfReview(Long id) {
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getBookingId() == getReviewById(id).getBookingId()) {
                BookingDto bookingDto = new BookingDto();
                bookingDto.setStart(booking.getStart());
                bookingDto.setEnd(booking.getEnd());
                bookingDto.setItemId(booking.getItemId());
                bookingDto.setStatusId(booking.getStatusId());
                return bookingDto;
            }
        }
        return null;
    }
}
