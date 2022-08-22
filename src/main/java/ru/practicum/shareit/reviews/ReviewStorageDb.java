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
        jdbcTemplate.update("INSERT INTO reviews (description, item, reviewer, date, evaluation, booking) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                review.getDescription(),
                review.getItem(),
                review.getReviewer(),
                review.getDate(),
                review.getEvaluation(),
                review.getBooking());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        jdbcTemplate.update("UPDATE reviews SET " +
                        "description = ?, item = ?, reviewer = ?, date = ?, evaluation = ?, booking = ? WHERE id = ?",
                review.getDescription(),
                review.getItem(),
                review.getReviewer(),
                review.getDate(),
                review.getEvaluation(),
                review.getId(),
                review.getBooking());
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE id = ?", id);
        if (reviewRows.next()) {
            Review review = new Review();
            review.setId(id);
            review.setDescription(reviewRows.getString("description"));
            review.setItem(reviewRows.getLong("item"));
            review.setReviewer((reviewRows.getLong("reviewer")));
            review.setDate(reviewRows.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBooking(reviewRows.getLong("booking"));
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
            review.setId(reviewRows.getLong("id"));
            review.setDescription(reviewRows.getString("description"));
            review.setItem(reviewRows.getLong("item"));
            review.setReviewer((reviewRows.getLong("reviewer")));
            review.setDate(reviewRows.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBooking(reviewRows.getLong("booking"));
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public void removeReviewById(Long id) {
        jdbcTemplate.update("DELETE * FROM reviews WHERE id = ?", id);
    }

    @Override
    public void removeAllReviews() {
        jdbcTemplate.update("DELETE * FROM reviews");
    }
}
