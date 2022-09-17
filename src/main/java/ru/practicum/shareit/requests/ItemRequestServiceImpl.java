package ru.practicum.shareit.requests;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final ItemRequestStorageDb itemRequestStorageDb;
    private final UserStorageInMemory userStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;

    public ItemRequestServiceImpl(ItemRequestStorageInMemory itemRequestStorageInMemory,
                                  ItemRequestStorageDb itemRequestStorageDb,
                                  UserStorageInMemory userStorageInMemory,
                                  ItemStorageInMemory itemStorageInMemory) {
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.itemRequestStorageDb = itemRequestStorageDb;
        this.userStorageInMemory = userStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        ItemRequest itemRequestInDb = itemRequestStorageDb.createItemRequest(itemRequest);
        itemRequestStorageInMemory.createItemRequest(itemRequestInDb);
        return itemRequestInDb;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        itemRequestStorageInMemory.updateItemRequest(itemRequest);
        itemRequestStorageDb.updateItemRequest(itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest getItemRequestById(Long id) {
        return itemRequestStorageInMemory.getItemRequestById(id);
    }

    @Override
    public Set<ItemRequest> getAllItemRequests() {
        return itemRequestStorageInMemory.getAllItemRequests();
    }

    @Override
    public void removeItemRequestById(Long id) {
        itemRequestStorageInMemory.removeItemRequestById(id);
        itemRequestStorageDb.removeItemRequestById(id);
    }

    @Override
    public void removeAllItemRequests() {
        itemRequestStorageInMemory.removeAllItemRequests();
        itemRequestStorageDb.removeAllItemRequests();
    }

    @Override
    public User getUserOfItemRequest(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == getItemRequestById(id).getUserId()) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Set<Item> getItemsOfItemRequest(Long id) {
        Set<Item> itemsOfItemRequest = new HashSet<>();
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getRequestId() == getItemRequestById(id).getItemRequestId()) {
                itemsOfItemRequest.add(item);
            }
        }
        return itemsOfItemRequest;
    }
}
