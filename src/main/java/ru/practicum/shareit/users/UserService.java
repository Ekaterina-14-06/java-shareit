package ru.practicum.shareit.users;

import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;

import java.util.Set;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Set<User> getAllUsers();

    void removeUserById(Long id);

    void removeAllUsers();

    Set<Item> getItemsOfUser(Long id);

    void removeItemsOfUser(Long id);

    Set<ItemRequest> getItemRequestsOfUser(Long id);

    void removeItemRequestsOfUser(Long id);

    Set<Booking> getBookingsOfUser(Long id);

    void removeBookingsOfUser(Long id);

    Set<Review> getReviewsOfUser(Long id);

    void removeReviewsOfUser(Long id);
}
