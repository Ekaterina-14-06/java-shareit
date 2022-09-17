package ru.practicum.shareit.requests;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Component
public class ItemRequestStorageDb implements ItemRequestStorage {
    private final JdbcTemplate jdbcTemplate;

    public ItemRequestStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        jdbcTemplate.update("INSERT INTO item_requests (description, user_id, created VALUES (?, ?, ?)",
                itemRequest.getDescription(), itemRequest.getUserId(), itemRequest.getCreated());
        return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        jdbcTemplate.update("UPDATE item_requests SET description = ?, user_id = ?, created = ?",
                itemRequest.getDescription(), itemRequest.getUserId(), itemRequest.getCreated());
        return itemRequest;
    }

    @Override
    public ItemRequest getItemRequestById(Long id) {
        SqlRowSet itemRequestRows = jdbcTemplate.queryForRowSet("SELECT * FROM item_requests WHERE item_request_id = ?", id);
        if (itemRequestRows.next()) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItemRequestId(id);
            itemRequest.setDescription(itemRequestRows.getString("description"));
            itemRequest.setUserId(itemRequestRows.getLong("user_id"));
            itemRequest.setCreated(itemRequestRows.getDate("created")
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            return itemRequest;
        } else {
            return null;
        }
    }

    @Override
    public Set<ItemRequest> getAllItemRequests() {
        Set<ItemRequest> itemRequests = new HashSet<>();
        SqlRowSet itemRequestRows = jdbcTemplate.queryForRowSet("SELECT * FROM item_requests");
        while (itemRequestRows.next()) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.setItemRequestId(itemRequestRows.getLong("item_request_id"));
            itemRequest.setDescription(itemRequestRows.getString("description"));
            itemRequest.setUserId(itemRequestRows.getLong("user_id"));
            itemRequest.setCreated((itemRequestRows.getDate("created"))
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            itemRequests.add(itemRequest);
        }
        return itemRequests;
    }

    @Override
    public void removeItemRequestById(Long id) {
        jdbcTemplate.update("DELETE * FROM item_requests WHERE item_request_id = ?", id);
    }

    @Override
    public void removeAllItemRequests() {
        jdbcTemplate.update("DELETE * FROM item_requests");
    }
}
