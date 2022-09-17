package ru.practicum.shareit.reviews;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusStorageInMemory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class ReviewStorageInMemory implements ReviewStorage {
    private final Set<Review> reviews = new HashSet<>();
    private final BookingStorageInMemory bookingStorageInMemory;
    private final StatusStorageInMemory statusStorageInMemory;

    public ReviewStorageInMemory(BookingStorageInMemory bookingStorageInMemory,
                                 StatusStorageInMemory statusStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.statusStorageInMemory = statusStorageInMemory;
    }

    @Override
    public Review createReview(Review review) {
        try {
            Long statusIdOfApproved = -1L;
            for (Status status : statusStorageInMemory.getAllStatuses()) {
                if (status.getName().equals("approved")) {
                    statusIdOfApproved = status.getStatusId();
                }
            }

            Boolean isBookingExistsAndApprovedAndEnds = false;
            for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                if (booking.getBookingId() == review.getBookingId()) {
                    if (booking.getStatusId() == statusIdOfApproved) {
                        if (booking.getEnd().isBefore(LocalDateTime.now())) {
                            isBookingExistsAndApprovedAndEnds = true;
                        }
                    }
                }
            }

            if (isBookingExistsAndApprovedAndEnds) {
                reviews.add(review);
                log.info("Добавлен отзыв {}", review);
                return review;
            } else {
                log.error("Попытка создания отзыва на вещь, которую ещё не вернули после бронирования или вообще не брали.");
                throw new ValidationException("Попытка создания отзыва на вещь, которую ещё не вернули после бронирования или вообще не брали. " +
                        "Отзыв не был обновлён.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Review updateReview(Review review) {
        try {
            boolean isPresent = false;
            for (Review revuewInReviews : reviews) {
                if (revuewInReviews.getReviewId() == review.getReviewId()) {
                    isPresent = true;
                    revuewInReviews.setDescription(review.getDescription());
                    revuewInReviews.setItemId(review.getItemId());
                    revuewInReviews.setUserId(review.getUserId());
                    revuewInReviews.setDate(review.getDate());
                    revuewInReviews.setEvaluation(review.getEvaluation());
                    revuewInReviews.setBookingId(review.getBookingId());
                    log.info("Обновлён отзыв {}", review);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменения значения полей несуществующего отзыва (нет совпадений по id {}).",
                        review.getReviewId());
                throw new ValidationException("Отзыва с таким id не существует (нечего обновлять). " +
                        "Значения полей отзыва не были обновлены.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return review;
    }

    @Override
    public Review getReviewById(Long id) {
        try {
            for (Review review : reviews) {
                if (review.getReviewId() == id) {
                    return review;
                }
            }

            log.error("Попытка получения данных несуществующего отзыва (нет совпадений по id ).");
            throw new ValidationException("Отзыв с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<Review> getAllReviews() {
        return reviews;
    }

    @Override
    public void removeReviewById(Long id) {
        try {
            for (Review review : reviews) {
                if (review.getReviewId() == id) {
                    reviews.remove(review);
                    break;
                }
            }

            log.error("Попытка удаления несуществующего отзыва (нет совпадений по id ).");
            throw new ValidationException("Отзыва с таким id не существует." +
                    "Запись об отзыве не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllReviews() {
        reviews.clear();
    }
}
