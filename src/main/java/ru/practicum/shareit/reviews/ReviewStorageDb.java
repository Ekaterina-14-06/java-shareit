package ru.practicum.shareit.reviews;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Component
public class ReviewStorageDb implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    public ReviewStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        jdbcTemplate.update("INSERT INTO reviews (description, item_id, user_id, date, evaluation, booking_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                review.getDescription(),
                review.getItemId(),
                review.getUserId(),
                review.getDate(),
                review.getEvaluation(),
                review.getBookingId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        jdbcTemplate.update("UPDATE reviews SET " +
                        "description = ?, item_id = ?, user_id = ?, date = ?, evaluation = ?, booking_id = ? " +
                        "WHERE review_id = ?",
                review.getDescription(),
                review.getItemId(),
                review.getUserId(),
                review.getDate(),
                review.getEvaluation(),
                review.getReviewId(),
                review.getBookingId());
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE review_id = ?", id);
        if (reviewRows.next()) {
            Review review = new Review();
            review.setReviewId(id);
            review.setDescription(reviewRows.getString("description"));
            review.setItemId(reviewRows.getLong("item_id"));
            review.setUserId((reviewRows.getLong("user_id")));
            review.setDate(reviewRows.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBookingId(reviewRows.getLong("booking_id"));
            return review;
        } else {
            return null;
        }
    }

    @Override
    public Set<Review> getAllReviews() {
        Set<Review> reviews = new HashSet<>();
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews");
        while (reviewRows.next()) {
            Review review = new Review();
            review.setReviewId(reviewRows.getLong("review_id"));
            review.setDescription(reviewRows.getString("description"));
            review.setItemId(reviewRows.getLong("item_id"));
            review.setUserId((reviewRows.getLong("user_id")));
            review.setDate(reviewRows.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBookingId(reviewRows.getLong("booking_id"));
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public void removeReviewById(Long id) {
        jdbcTemplate.update("DELETE * FROM reviews WHERE review_id = ?", id);
    }

    @Override
    public void removeAllReviews() {
        jdbcTemplate.update("DELETE * FROM reviews");
    }
}
