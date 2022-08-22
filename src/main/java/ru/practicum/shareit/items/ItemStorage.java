package ru.practicum.shareit.items;

import java.util.Set;

public interface ItemStorage {
    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long id);

    Set<Item> getAllItems();

    void removeItemById(Long id);

    void removeAllItems();
}
