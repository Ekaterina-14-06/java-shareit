package ru.practicum.shareit.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.users.User;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Service
public class ItemServiceDao implements ItemService {
    private final ItemStorageInMemory itemStorageInMemory;
    private final ItemStorageDb itemStorageDb;

    @Autowired
    public ItemServiceDao(ItemStorageInMemory itemStorageInMemory,
                          ItemStorageDb itemStorageDb) {
        this.itemStorageInMemory = itemStorageInMemory;
        this.itemStorageDb = itemStorageDb;
    }

    @Override
    public Item createItem(Item item) {
        Item itemInDb = itemStorageDb.createItem(item);
        itemStorageInMemory.createItem(itemInDb);
        return itemInDb;
    }

    @Override
    public Item updateItem(Item item) {
        itemStorageInMemory.updateItem(item);
        itemStorageDb.updateItem(item);
        return item;
    }

    @Override
    public Item getItemById(Long id) {
        return itemStorageInMemory.getItemById(id);
    }

    @Override
    public Set<Item> getAllItems() {
        return itemStorageInMemory.getAllItems();
    }

    @Override
    public void removeItemById(Long id) {
        itemStorageInMemory.removeItemById(id);
        itemStorageDb.removeItemById(id);
    }

    @Override
    public void removeAllItems() {
        itemStorageInMemory.removeAllItems();
        itemStorageDb.removeAllItems();
    }

    @Override
    public User getUserOfItem(Long id) {
        SqlRowSet userRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE user_id = ?", getItemById(id).getUserId());
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
    public Set<Review> getReviewsOfItem(Long id) {
        Set<Review> reviews = new HashSet<>();
        SqlRowSet reviewRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM reviews WHERE item_id = ?", id);
        while (reviewRows.next()) {
            Review review = new Review();
            review.setReviewId(reviewRows.getLong("review_id"));
            review.setDescription(reviewRows.getString("description"));
            review.setItemId(reviewRows.getLong("item_id"));
            review.setUserId(reviewRows.getLong("user_id"));
            review.setDate(reviewRows.getDate("date")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBookingId(reviewRows.getLong("booking_id"));
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public ItemRequest getItemRequestOfItem(Long id) {
        SqlRowSet itemRequestRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM item_requests WHERE item_request_id = ?", getItemById(id).getRequestId());
        while (itemRequestRows.next()) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItemRequestId(itemRequestRows.getLong("item_request_id"));
            itemRequest.setDescription(itemRequestRows.getString("description"));
            itemRequest.setUserId(itemRequestRows.getLong("user_id"));
            itemRequest.setCreated(itemRequestRows.getDate("created")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            return itemRequest;
        }
        return null;
    }

    @Override
    public Set<Booking> getBookingsOfItem(Long id) {
        Set<Booking> bookings = new HashSet<>();
        SqlRowSet bookingRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM bookings WHERE item_id = ?", id);
        while (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setBookingId(bookingRows.getLong("booking_id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItemId(bookingRows.getLong("item_id"));
            booking.setUserId(bookingRows.getLong("user_id"));
            booking.setStatusId(bookingRows.getLong("status_id"));
            bookings.add(booking);
        }
        return bookings;
    }
}
