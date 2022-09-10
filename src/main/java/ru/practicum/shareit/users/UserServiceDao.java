package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceDao implements UserService {
    private final UserStorageInMemory userStorageInMemory;
    private final UserStorageDb userStorageDb;

    @Autowired
    public UserServiceDao(UserStorageInMemory userStorageInMemory,
                          UserStorageDb userStorageDb) {
        this.userStorageInMemory = userStorageInMemory;
        this.userStorageDb = userStorageDb;
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
        Set<Item> items = new HashSet<>();
        SqlRowSet itemRows = userStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM items WHERE user_id = ?", id);
        while (itemRows.next()) {
            Item item = new Item();
            item.setItemId(itemRows.getLong("item_id"));
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequestId(itemRows.getLong("request_id"));
            item.setUserId(id);
            items.add(item);
        }
        return items;
    }

    @Override
    public void removeItemsOfUser(Long id) {
        userStorageDb.getJdbcTemplate().update("DELETE * FROM items WHERE user_id = ?", id);
    }

    @Override
    public Set<ItemRequest> getItemRequestsOfUser(Long id) {
        Set<ItemRequest> itemRequests = new HashSet<>();
        SqlRowSet itemRequestRows = userStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM item_requests WHERE user_id = ?", id);
        while (itemRequestRows.next()) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItemRequestId(itemRequestRows.getLong("item_request_id"));
            itemRequest.setDescription(itemRequestRows.getString("description"));
            itemRequest.setCreated(itemRequestRows.getDate("created")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            itemRequest.setUserId(id);
            itemRequests.add(itemRequest);
        }
        return itemRequests;
    }

    @Override
    public void removeItemRequestsOfUser(Long id) {
        userStorageDb.getJdbcTemplate().update("DELETE * FROM item_requests WHERE user_id = ?", id);
    }

    @Override
    public Set<Booking> getBookingsOfUser(Long id) {
        Set<Booking> bookings = new HashSet<>();
        SqlRowSet bookingRows = userStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM bookings WHERE user_id = ?", id);
        while (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setBookingId(bookingRows.getLong("booking_id"));
            booking.setItemId(bookingRows.getLong("item_id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setStatusId(bookingRows.getLong("status_id"));
            booking.setUserId(id);
            bookings.add(booking);
        }
        return bookings;
    }

    @Override
    public void removeBookingsOfUser(Long id) {
        userStorageDb.getJdbcTemplate().update("DELETE * FROM bookings WHERE user_id = ?", id);
    }

    @Override
    public Set<Review> getReviewsOfUser(Long id) {
        Set<Review> reviews = new HashSet<>();
        SqlRowSet reviewsRows = userStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM reviews WHERE user_id = ?", id);
        while (reviewsRows.next()) {
            Review review = new Review();
            review.setReviewId(reviewsRows.getLong("review_id"));
            review.setItemId(reviewsRows.getLong("item_id"));
            review.setDescription(reviewsRows.getString("description"));
            review.setDate(reviewsRows.getDate("date")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewsRows.getBoolean("evaluation"));
            review.setUserId(id);
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public void removeReviewsOfUser(Long id) {
        userStorageDb.getJdbcTemplate().update("DELETE * FROM reviews WHERE user_id = ?", id);
    }
}