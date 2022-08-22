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

    //=================================================== CRUD =======================================================

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

    //=============================================== БИЗНЕС-ЛОГИКА ===================================================

    @Override
    public User getUserOfItem(Long id) {
        SqlRowSet userRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE id = ?", getItemById(id).getOwner());
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
    public Set<Review> getReviewsOfItem(Long id) {
        Set<Review> reviews = new HashSet<>();
        SqlRowSet reviewRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM reviews WHERE item = ?", id);
        while (reviewRows.next()) {
            Review review = new Review();
            review.setId(reviewRows.getLong("id"));
            review.setDescription(reviewRows.getString("description"));
            review.setItem(reviewRows.getLong("item"));
            review.setReviewer(reviewRows.getLong("reviewer"));
            review.setDate(reviewRows.getDate("date")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            review.setEvaluation(reviewRows.getBoolean("evaluation"));
            review.setBooking(reviewRows.getLong("booking"));
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public ItemRequest getItemRequestOfItem(Long id) {
        SqlRowSet itemRequestRows = itemStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM item_requests WHERE id = ?", getItemById(id).getRequest());
        while (itemRequestRows.next()) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setId(itemRequestRows.getLong("id"));
            itemRequest.setDescription(itemRequestRows.getString("description"));
            itemRequest.setRequestor(itemRequestRows.getLong("requestor"));
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
                "SELECT * FROM bookings WHERE item = ?", id);
        while (bookingRows.next()) {
            Booking booking = new Booking();
            booking.setId(bookingRows.getLong("id"));
            booking.setStart(bookingRows.getDate("start")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setEnd(bookingRows.getDate("end")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            booking.setItem(bookingRows.getLong("item"));
            booking.setBooker(bookingRows.getLong("booker"));
            booking.setStatus(bookingRows.getLong("status"));
            bookings.add(booking);
        }
        return bookings;
    }
}
