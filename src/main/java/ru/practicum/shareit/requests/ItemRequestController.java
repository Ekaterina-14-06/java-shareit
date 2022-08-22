package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.users.User;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestServiceImpl;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestServiceImpl) {
        this.itemRequestServiceImpl = itemRequestServiceImpl;
    }

    @PostMapping()
    public void addItemRequest(@Valid @RequestBody ItemRequest itemRequest) {
        itemRequestServiceImpl.createItemRequest(itemRequest);
    }

    @PutMapping()
    public void updateItemRequest(@Valid @RequestBody ItemRequest itemRequest) {
        itemRequestServiceImpl.updateItemRequest(itemRequest);
    }

    @DeleteMapping("/{id}")
    public void removeItemRequestById(@PathVariable("id") Long id) {
        itemRequestServiceImpl.removeItemRequestById(id);
    }

    @DeleteMapping()
    public void removeAllItemRequests() {
        itemRequestServiceImpl.removeAllItemRequests();
    }

    @GetMapping("/{id}")
    public ItemRequest getIteRequestById(@PathVariable("id") Long id) {
        return itemRequestServiceImpl.getItemRequestById(id);
    }

    @GetMapping()
    public Set<ItemRequest> getAllItemRequests() {
        return itemRequestServiceImpl.getAllItemRequests();
    }

    @GetMapping("/{id}/users")
    public User getUserOfItemRequest(@PathVariable("id") Long id) {
        return itemRequestServiceImpl.getUserOfItemRequest(id);
    }

    @GetMapping("/{id}/items")
    public Set<Item> getItemsOfItemRequest(@PathVariable("id") Long id) {
        return itemRequestServiceImpl.getItemsOfItemRequest(id);
    }
}
