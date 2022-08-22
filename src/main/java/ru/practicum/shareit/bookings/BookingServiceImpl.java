package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.statuses.StatusStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final BookingStorageDb bookingStorageDb;
    private final ItemStorageInMemory itemStorageInMemory;
    private final UserStorageInMemory userStorageInMemory;
    private final StatusStorageInMemory statusStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;

    @Autowired
    public BookingServiceImpl(BookingStorageInMemory bookingStorageInMemory,
                              BookingStorageDb bookingStorageDb,
                              ItemStorageInMemory itemStorageInMemory,
                              UserStorageInMemory userStorageInMemory,
                              StatusStorageInMemory statusStorageInMemory,
                              ReviewStorageInMemory reviewStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.bookingStorageDb = bookingStorageDb;
        this.itemStorageInMemory = itemStorageInMemory;
        this.userStorageInMemory = userStorageInMemory;
        this.statusStorageInMemory = statusStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
    }

    @Override
    public Booking createBooking(Booking booking) {
        Booking bookingInDb = bookingStorageDb.createBooking(booking);
        bookingStorageInMemory.createBooking(bookingInDb);
        return bookingInDb;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        bookingStorageInMemory.updateBooking(booking);
        bookingStorageDb.updateBooking(booking);
        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingStorageInMemory.getBookingById(id);
    }

    @Override
    public Set<Booking> getAllBookings() {
        return bookingStorageInMemory.getAllBookings();
    }

    @Override
    public void removeBookingById(Long id) {
        bookingStorageInMemory.removeBookingById(id);
        bookingStorageDb.removeBookingById(id);
    }

    @Override
    public void removeAllBookings() {
        bookingStorageInMemory.removeAllBookings();
        bookingStorageDb.removeAllBookings();
    }

    @Override
    public Item getItemOfBooking(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getId() == getBookingById(id).getItem()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public User getUserOfBooking(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getId() == getBookingById(id).getBooker()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Status getStatusOfBooking(Long id) {
        for (Status status : statusStorageInMemory.getAllStatuses()) {
            if (status.getId() == getBookingById(id).getStatus()) {
                return status;
            }
        }
        return null;
    }

    @Override
    public Review getReviewOfBooking(Long id) {
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getBooking() == id) {
                return review;
            }
        }
        return null;
    }
}
