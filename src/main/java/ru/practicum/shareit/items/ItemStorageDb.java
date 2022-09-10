package ru.practicum.shareit.items;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ItemStorageDb implements ItemStorage {
    private final JdbcTemplate jdbcTemplate;

    public ItemStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Item createItem(Item item) {
        jdbcTemplate.update("INSERT INTO items (name, description, user_id, available, request_id) VALUES (?, ?, ?, ?, ?)",
                item.getName(), item.getDescription(), item.getUserId(), item.getAvailable(), item.getRequestId());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        jdbcTemplate.update("UPDATE items SET name = ?, description = ?, user_id = ?, available = ?, request_id = ? WHERE item_id = ?",
                item.getName(), item.getDescription(), item.getUserId(), item.getAvailable(), item.getRequestId(), item.getItemId());
        return item;
    }

    @Override
    public Item getItemById(Long id) {
        SqlRowSet itemRows = jdbcTemplate.queryForRowSet("SELECT * FROM items WHERE item_id = ?", id);
        if (itemRows.next()) {
            Item item = new Item();
            item.setItemId(id);
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setUserId(itemRows.getLong("user_id"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequestId(itemRows.getLong("request_id"));
            return item;
        } else {
            return null;
        }
    }

    @Override
    public Set<Item> getAllItems() {
        Set<Item> items = new HashSet<>();
        SqlRowSet itemRows = jdbcTemplate.queryForRowSet("SELECT * FROM items");
        while (itemRows.next()) {
            Item item = new Item();
            item.setName(itemRows.getString("name"));
            item.setDescription(itemRows.getString("description"));
            item.setUserId(itemRows.getLong("user_id"));
            item.setAvailable(itemRows.getBoolean("available"));
            item.setRequestId(itemRows.getLong("request_id"));
            items.add(item);
        }
        return items;
    }

    @Override
    public void removeItemById(Long id) {
        jdbcTemplate.update("DELETE * FROM items WHERE item_id = ?", id);
    }

    @Override
    public void removeAllItems() {
        jdbcTemplate.update("DELETE * FROM items");
    }
}
