package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingDto;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemDto;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestDto;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final UserServiceDto userServiceDto;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl,
                          UserServiceDto userServiceDto) {
        this.userServiceImpl = userServiceImpl;
        this.userServiceDto = userServiceDto;
    }

    @PostMapping()
    public void addUser(@Valid @RequestBody User user) {
        userServiceImpl.createUser(user);
    }

    @PutMapping()
    public void updateUser(@Valid @RequestBody User user) {
        userServiceImpl.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable("id") Long id) {
        userServiceImpl.removeUserById(id);
    }

    @DeleteMapping()
    public void removeAllUsers() {
        userServiceImpl.removeAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long id) {
        return userServiceDto.getUserById(id);
    }

    @GetMapping()
    public Set<UserDto> getAllUsers() {
        return userServiceDto.getAllUsers();
    }

    @GetMapping("/{id}/items")
    public Set<ItemDto> getItemsOfUser(@PathVariable("id") Long id) {
        return userServiceDto.getItemsOfUser(id);
    }

    @DeleteMapping("/{id}/items")
    public void removeItemsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeItemsOfUser(id);
    }

    @GetMapping("/{id}/itemRequests")
    public Set<ItemRequestDto> getItemRequestsOfUser(@PathVariable("id") Long id) {
        return userServiceDto.getItemRequestsOfUser(id);
    }

    @DeleteMapping("/{id}/itemRequests")
    public void removeItemRequestsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeItemRequestsOfUser(id);
    }

    @GetMapping("/{id}/bookings")
    public Set<BookingDto> getBookingsOfUser(@PathVariable("id") Long id) {
        return userServiceDto.getBookingsOfUser(id);
    }

    @DeleteMapping("/{id}/bookings")
    public void removeBookingsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeBookingsOfUser(id);
    }

    @GetMapping("/{id}/reviews")
    public Set<ReviewDto> getReviewsOfUser(@PathVariable("id") Long id) {
        return userServiceDto.getReviewsOfUser(id);
    }

    @DeleteMapping("/{id}/reviews")
    public void removeReviewsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeReviewsOfUser(id);
    }
}
