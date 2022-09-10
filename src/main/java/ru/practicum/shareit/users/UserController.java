package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;

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
    public Set<UserDto> getAllUserDtos() {
        return userServiceDto.getAllUserDtos();
    }

    @GetMapping("/{id}/items")
    public Set<Item> getItemsOfUser(@PathVariable("id") Long id) {
        return userServiceImpl.getItemsOfUser(id);
    }

    @DeleteMapping("/{id}/items")
    public void removeItemsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeItemsOfUser(id);
    }

    @GetMapping("/{id}/itemRequests")
    public Set<ItemRequest> getItemRequestsOfUser(@PathVariable("id") Long id) {
        return userServiceImpl.getItemRequestsOfUser(id);
    }

    @DeleteMapping("/{id}/itemRequests")
    public void removeItemRequestsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeItemRequestsOfUser(id);
    }

    @GetMapping("/{id}/bookings")
    public Set<Booking> getBookingsOfUser(@PathVariable("id") Long id) {
        return userServiceImpl.getBookingsOfUser(id);
    }

    @DeleteMapping("/{id}/bookings")
    public void removeBookingsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeBookingsOfUser(id);
    }

    @GetMapping("/{id}/reviews")
    public Set<Review> getReviewsOfUser(@PathVariable("id") Long id) {
        return userServiceImpl.getReviewsOfUser(id);
    }

    @DeleteMapping("/{id}/reviews")
    public void removeReviewsOfUser(@PathVariable("id") Long id) {
        userServiceImpl.removeReviewsOfUser(id);
    }
}
