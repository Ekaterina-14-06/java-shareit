package ru.practicum.shareit.items;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingStorageInMemory;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorageInMemory;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorageInMemory itemStorageInMemory;
    private final ItemStorageDb itemStorageDb;
    private final UserStorageInMemory userStorageInMemory;
    private final ReviewStorageInMemory reviewStorageInMemory;
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;

    @Autowired
    public ItemServiceImpl(ItemStorageInMemory itemStorageInMemory,
                           ItemStorageDb itemStorageDb,
                           UserStorageInMemory userStorageInMemory,
                           ReviewStorageInMemory reviewStorageInMemory,
                           ItemRequestStorageInMemory itemRequestStorageInMemory,
                           BookingStorageInMemory bookingStorageInMemory) {
        this.itemStorageInMemory = itemStorageInMemory;
        this.itemStorageDb = itemStorageDb;
        this.userStorageInMemory = userStorageInMemory;
        this.reviewStorageInMemory = reviewStorageInMemory;
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
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

    public Set<Item> getAvailableItems() {
        Set<Item> availableItems = new HashSet<>();
        for (Item item : getAllItems()) {
            if (item.getAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
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
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getId() == getItemById(id).getOwner()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Set<Review> getReviewsOfItem(Long id) {
        Set<Review> reviewsOfItem = new HashSet<>();
        for (Review review : reviewStorageInMemory.getAllReviews()) {
            if (review.getItem() == id) {
                reviewsOfItem.add(review);
            }
        }
        return reviewsOfItem;
    }

    @Override
    public ItemRequest getItemRequestOfItem(Long id) {
        for (ItemRequest itemRequest : itemRequestStorageInMemory.getAllItemRequests()) {
            if (itemRequest.getId() == getItemById(id).getRequest()) {
                return itemRequest;
            }
        }
        return null;
    }

    @Override
    public Set<Booking> getBookingsOfItem(Long id) {
        Set<Booking> bookingsOfItem = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getItem() == id) {
                bookingsOfItem.add(booking);
            }
        }
        return bookingsOfItem;
    }

    public void updateItemById(Long itemId,
                               Long userId,
                               Item item) {
        try {
            if (getItemById(itemId).getOwner() == userId) {
                updateItem(item);
            } else {
                log.error("Попытка изменения значений полей вещи не её владельцем.");
                throw new ValidationException("Попытка изменения значений полей вещи не её владельцем. Операция отменена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public Set<Item> findItemById(String text) {
        Set<Item> items = new HashSet<>();
        for (Item item : getAllItems()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                items.add(item);
            }
        }
        return items;
    }

    public Set<ItemOfOwner> getItemsOfOwner(Long userId) {
        Set<ItemOfOwner> items = new HashSet<>();
        for (Item item : getAllItems()) {
            if (item.getOwner() == userId) {
                ItemOfOwner itemOfOwner = new ItemOfOwner();
                itemOfOwner.setItem(item);
                itemOfOwner.setLast(LocalDateTime.now());
                itemOfOwner.setNext(LocalDateTime.now());

                for (Booking booking : bookingStorageInMemory.getAllBookings()) {
                    if (booking.getItem() == item.getId()) {
                        if (booking.getEnd().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(itemOfOwner.getLast())) {
                            itemOfOwner.setLast(booking.getEnd());
                        }

                        if (booking.getStart().isAfter(LocalDateTime.now()) && booking.getStart().isBefore(itemOfOwner.getNext())) {
                            itemOfOwner.setNext(booking.getStart());
                        }
                    }
                }
                items.add(itemOfOwner);
            }
        }
        return items;
    }
}

@Data
class ItemOfOwner {
    private Item item;
    private LocalDateTime last;
    private LocalDateTime next;
}
