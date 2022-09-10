package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorageInMemory userStorageInMemory;
    private final UserStorageDb userStorageDb;
    private final ItemStorageInMemory itemStorageInMemory;
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;

    @Autowired
    public UserServiceImpl(UserStorageInMemory userStorageInMemory,
                           UserStorageDb userStorageDb,
                           ItemStorageInMemory itemStorageInMemory,
                           ItemRequestStorageInMemory itemRequestStorageInMemory,
                           BookingStorageInMemory bookingStorageInMemory,
                           ReviewStorageInMemory reviewStorageInMemory) {
        this.userStorageInMemory = userStorageInMemory;
        this.userStorageDb = userStorageDb;
        this.itemStorageInMemory = itemStorageInMemory;
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
    }

    @Override
    public User createUser(User user) {
        User userInDb = userStorageDb.createUser(user);
        userStorageInMemory.createUser(userInDb);
        return userInDb;
    }

    @Override
    public User updateUser(User user) {
        userStorageInMemory.updateUser(user);
        userStorageDb.updateUser(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userStorageInMemory.getUserById(id);
    }

    @Override
    public Set<User> getAllUsers() {
        return userStorageInMemory.getAllUsers();
    }

    @Override
    public void removeUserById(Long id) {
        userStorageInMemory.removeUserById(id);
        userStorageDb.removeUserById(id);
    }

    @Override
    public void removeAllUsers() {
        userStorageInMemory.removeAllUsers();
        userStorageDb.removeAllUsers();
    }

    @Override
    public Set<Item> getItemsOfUser(Long id) {
        Set<Item> itemsOfUser = new HashSet<>();
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getUserId() == id) {
                itemsOfUser.add(item);
            }
        }
        return itemsOfUser;
    }

    @Override
    public void removeItemsOfUser(Long id) {
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getUserId() == id) {
                itemStorageInMemory.getAllItems().remove(item);
            }
        }
    }

    @Override
    public Set<ItemRequest> getItemRequestsOfUser(Long id) {
        Set<ItemRequest> itemRequestsOfUser = new HashSet<>();
        for (ItemRequest itemRequest : itemRequestStorageInMemory.getAllItemRequests()) {
            if (itemRequest.getUserId() == id) {
                itemRequestsOfUser.add(itemRequest);
            }
        }
        return itemRequestsOfUser;
    }

    @Override
    public void removeItemRequestsOfUser(Long id) {
        for (ItemRequest itemRequest : itemRequestStorageInMemory.getAllItemRequests()) {
            if (itemRequest.getUserId() == id) {
                itemRequestStorageInMemory.getAllItemRequests().remove(itemRequest);
            }
        }
    }

    @Override
    public Set<Booking> getBookingsOfUser(Long id) {
        Set<Booking> bookingsOfUser = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getUserId() == id) {
                bookingsOfUser.add(booking);
            }
        }
        return bookingsOfUser;
    }

    @Override
    public void removeBookingsOfUser(Long id) {
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getUserId() == id) {
                bookingStorageInMemory.getAllBookings().remove(booking);
            }
        }
    }

    @Override
    public Set<Review> getReviewsOfUser(Long id) {
        Set<Review> reviewsOfUser = new HashSet<>();
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getUserId() == id) {
                reviewsOfUser.add(review);
            }
        }
        return reviewsOfUser;
    }

    @Override
    public void removeReviewsOfUser(Long id) {
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getUserId() == id) {
                reviewStorageInMemory.getAllReviews().remove(review);
            }
        }
    }
}