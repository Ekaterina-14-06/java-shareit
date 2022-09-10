package ru.practicum.shareit.requests;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class ItemRequestServiceDao implements ItemRequestService {
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final ItemRequestStorageDb itemRequestStorageDb;
    private final UserStorageInMemory userStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;

    public ItemRequestServiceDao(ItemRequestStorageInMemory itemRequestStorageInMemory,
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
        SqlRowSet userRows = itemRequestStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM users WHERE user_id = ?", getItemRequestById(id).getUserId());
        if (userRows.next()) {
            User user = new User();
            user.setUserId(userRows.getLong("user_id"));
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            return user;
        }
        return null;
    }

    @Override
    public Set<Item> getItemsOfItemRequest(Long id) {
        Set<Item> items = new HashSet<>();
        SqlRowSet itemRows = itemRequestStorageDb.getJdbcTemplate().queryForRowSet(
                "SELECT * FROM items WHERE request_id = ?", getItemRequestById(id).getItemRequestId());
        while (itemRows.next()) {
            Item item = new Item();
            item.setItemId(itemRows.getLong("item_id"));
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setUserId(itemRows.getLong("user_id"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequestId(itemRows.getLong("request_id"));
            items.add(item);
        }
        return items;
    }
}