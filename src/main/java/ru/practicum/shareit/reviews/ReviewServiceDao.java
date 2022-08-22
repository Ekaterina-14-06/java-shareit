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
        SqlRowSet userRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE id = ?", getReviewById(id).getReviewer());
        if (userRows.next()) {
            User user = new User();
            user.setId(userRows.getLong("id"));
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            return user;
        }
        return null;
    }

    @Override
    public Item getItemOfReview(Long id) {
        SqlRowSet itemRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM items WHERE id = ?", getReviewById(id).getItem());
        if (itemRows.next()) {
            Item item = new Item();
            item.setId(itemRows.getLong("id"));
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setOwner(itemRows.getLong("owner"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequest(itemRows.getLong("request"));
            return item;
        }
        return null;
    }

    @Override
    public Booking getBookingOfReview(Long id) {
        SqlRowSet bookingRows = reviewStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM bookings WHERE id = ?", getReviewById(id).getBooking());
        if (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setId(bookingRows.getLong("id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItem(bookingRows.getLong("item"));
            booking.setBooker(bookingRows.getLong("booker"));
            booking.setStatus(bookingRows.getLong("status"));
            return booking;
        }
        return null;
    }
}