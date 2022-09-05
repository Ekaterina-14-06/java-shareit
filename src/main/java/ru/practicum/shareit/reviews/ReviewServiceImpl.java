package ru.practicum.shareit.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorageInMemory reviewStorageInMemory;
    private final ReviewStorageDb reviewStorageDb;
    private final UserStorageInMemory userStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;
    private final StatusStorageInMemory statusStorageInMemory;

    @Autowired
    public ReviewServiceImpl(ReviewStorageInMemory reviewStorageInMemory,
                             ReviewStorageDb reviewStorageDb,
                             UserStorageInMemory userStorageInMemory,
                             ItemStorageInMemory itemStorageInMemory,
                             BookingStorageInMemory bookingStorageInMemory,
                             StatusStorageInMemory statusStorageInMemory) {
        this.reviewStorageInMemory = reviewStorageInMemory;
        this.reviewStorageDb = reviewStorageDb;
        this.userStorageInMemory = userStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.statusStorageInMemory = statusStorageInMemory;
    }

    //=================================================== CRUD =======================================================

    @Override
    public Review createReview(Review review) {
        Review reviewInDb = reviewStorageDb.createReview(review);
        reviewStorageInMemory.createReview(reviewInDb);
        return reviewInDb;
    }

    @Override
    public Review updateReview(Review review) {
        reviewStorageInMemory.updateReview(review);
        reviewStorageDb.updateReview(review);
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewStorageInMemory.getReviewById(id);
    }

    @Override
    public Set<Review> getAllReviews() {
        return reviewStorageInMemory.getAllReviews();
    }

    @Override
    public void removeReviewById(Long id) {
        reviewStorageInMemory.removeReviewById(id);
        reviewStorageDb.removeReviewById(id);
    }

    @Override
    public void removeAllReviews() {
        reviewStorageInMemory.removeAllReviews();
        reviewStorageDb.removeAllReviews();
    }

    //=============================================== БИЗНЕС-ЛОГИКА ===================================================

    @Override
    public User getUserOfReview(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getId() == getReviewById(id).getReviewer()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Item getItemOfReview(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getId() == getReviewById(id).getItem()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Booking getBookingOfReview(Long id) {
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getId() == getReviewById(id).getBooking()) {
                return booking;
            }
        }
        return null;
    }

    public void addComment(Long itemId, Long userId, Review review) {
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getItem() == itemId &&
                booking.getBooker() == userId &&
                booking.getStatus() == statusStorageInMemory.getStatusIdByName("approved") &&
                booking.getEnd().isBefore(LocalDateTime.now())) {

                review.setItem(itemId);
                review.setReviewer(userId);
                Review reviewInDb = reviewStorageDb.createReview(review);
                reviewStorageInMemory.createReview(reviewInDb);
                break;
            }
        }
    }
}
