package ru.practicum.shareit.items;

import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.users.User;

import java.util.Set;

public interface ItemService {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long id);

    Set<Item> getAllItems();

    void removeItemById(Long id);

    void removeAllItems();

    User getUserOfItem(Long id);

    Set<Review> getReviewsOfItem(Long id);

    ItemRequest getItemRequestOfItem(Long id);

    Set<Booking> getBookingsOfItem(Long id);
}
