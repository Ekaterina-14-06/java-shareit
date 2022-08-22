package ru.practicum.shareit.requests;

import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.users.User;

import java.util.Set;

public interface ItemRequestService {
    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    ItemRequest getItemRequestById(Long id);

    Set<ItemRequest> getAllItemRequests();

    void removeItemRequestById(Long id);

    void removeAllItemRequests();

    User getUserOfItemRequest(Long id);

    Set<Item> getItemsOfItemRequest(Long id);
}
