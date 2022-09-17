package ru.practicum.shareit.requests;

import java.util.Set;

public interface ItemRequestStorage {
    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    ItemRequest getItemRequestById(Long id);

    Set<ItemRequest> getAllItemRequests();

    void removeItemRequestById(Long id);

    void removeAllItemRequests();
}
