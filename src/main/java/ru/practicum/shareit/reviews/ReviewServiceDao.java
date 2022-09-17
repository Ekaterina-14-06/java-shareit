package ru.practicum.shareit.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.users.User;

import java.time.ZoneId;
import java.util.Set;

@Service
public class ReviewServiceDao implements ReviewService {
    private final ReviewStorageInMemory reviewStorageInMemory;
    private final ReviewStorageDb reviewStorageDb;

    @Autowired
    public ReviewServiceDao(ReviewStorageInMemory reviewStorageInMemory,
                            ReviewStorageDb reviewStorageDb) {
        this.reviewStorageInMemory = reviewStorageInMemory;
        this.reviewStorageDb = reviewStorageDb;
    }

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

    @Override
    public User getUserOfReview(Long id) {
        SqlRowSet userRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE user_id = ?", getReviewById(id).getUserId());
        if (userRows.next()) {
            User user = new User();
            user.setUserId(userRows.getLong("user_id"));
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            return user;
        }
        return null;
    }

    @Override
    public Item getItemOfReview(Long id) {
        SqlRowSet itemRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM items WHERE item_id = ?", getReviewById(id).getItemId());
        if (itemRows.next()) {
            Item item = new Item();
            item.setItemId(itemRows.getLong("item_id"));
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setUserId(itemRows.getLong("user_id"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequestId(itemRows.getLong("request_id"));
            return item;
        }
        return null;
    }

    @Override
    public Booking getBookingOfReview(Long id) {
        SqlRowSet bookingRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM bookings WHERE booking_id = ?", getReviewById(id).getBookingId());
        if (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setBookingId(bookingRows.getLong("booking_id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItemId(bookingRows.getLong("item_id"));
            booking.setUserId(bookingRows.getLong("user_id"));
            booking.setStatusId(bookingRows.getLong("status_id"));
            return booking;
        }
        return null;
    }
}