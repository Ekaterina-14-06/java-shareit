package ru.practicum.shareit.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.reviews.ReviewServiceImpl;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final ReviewServiceImpl reviewServiceImpl;
    private final ItemServiceDto itemServiceDto;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl,
                          ReviewServiceImpl reviewServiceImpl,
                          ItemServiceDto itemServiceDto) {
        this.itemServiceImpl = itemServiceImpl;
        this.reviewServiceImpl = reviewServiceImpl;
        this.itemServiceDto = itemServiceDto;
    }

    @PostMapping()
    public void addItem(@Valid @RequestBody Item item) {
        itemServiceImpl.createItem(item);
    }

    @PutMapping()
    public void updateItem(@Valid @RequestBody Item item) {
        itemServiceImpl.updateItem(item);
    }

    @PatchMapping("/{itemId}")
    public void updateItemById(@PathVariable("itemId") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId,
                               @Valid @RequestBody Item item) {
        itemServiceImpl.updateItemById(itemId, userId, item);
    }

    @DeleteMapping("/{id}")
    public void removeItemById(@PathVariable("id") Long id) {
        itemServiceImpl.removeItemById(id);
    }

    @DeleteMapping()
    public void removeAllItems() {
        itemServiceImpl.removeAllItems();
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") Long id) {
        return itemServiceDto.getItemById(id);
    }

    @GetMapping()
    public Set<ItemOfOwner> getItemsOfOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemServiceImpl.getItemsOfOwner(userId);
    }

    @GetMapping("/{id}/user")
    public UserDto getUserOfItem(@PathVariable("id") Long id) {
        return itemServiceDto.getUserOfItem(id);
    }

    @GetMapping("/{id}/reviews")
    public Set<Review> getReviewsOfItem(@PathVariable("id") Long id) {
        return itemServiceImpl.getReviewsOfItem(id);
    }

    @GetMapping("/{id}/itemRequest")
    public ItemRequest getItemRequestOfItem(@PathVariable("id") Long id) {
        return itemServiceImpl.getItemRequestOfItem(id);
    }

    @GetMapping("/{id}/bookings")
    public Set<Booking> getBookingsOfItem(@PathVariable("id") Long id) {
        return itemServiceImpl.getBookingsOfItem(id);
    }

    @GetMapping("/search")
    public Set<Item> findItemById(@RequestParam("text") String text) {
        return itemServiceImpl.findItemById(text);
    }

    @PostMapping("/{itemId}/comment")
    public void addComment(@PathVariable("itemId") Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody Review review) {
        reviewServiceImpl.addComment(itemId, userId, review);
    }
}
