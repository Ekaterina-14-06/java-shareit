package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemDto;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestServiceImpl;
    private final ItemRequestServiceDto itemRequestServiceDto;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestServiceImpl,
                                 ItemRequestServiceDto itemRequestServiceDto) {
        this.itemRequestServiceImpl = itemRequestServiceImpl;
        this.itemRequestServiceDto = itemRequestServiceDto;
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
    public ItemRequestDto getIteRequestById(@PathVariable("id") Long id) {
        return itemRequestServiceDto.getItemRequestById(id);
    }

    @GetMapping()
    public Set<ItemRequestDto> getAllItemRequests() {
        return itemRequestServiceDto.getAllItemRequests();
    }

    @GetMapping("/{id}/users")
    public UserDto getUserOfItemRequest(@PathVariable("id") Long id) {
        return itemRequestServiceDto.getUserOfItemRequest(id);
    }

    @GetMapping("/{id}/items")
    public Set<ItemDto> getItemsOfItemRequest(@PathVariable("id") Long id) {
        return itemRequestServiceDto.getItemsOfItemRequest(id);
    }
}
