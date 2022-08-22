package ru.practicum.shareit.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserStorageDb implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO users (name, email) VALUES (?, ?)",
                user.getName(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE users SET name = ?, email = ? WHERE id = ?",
                user.getName(), user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        if (userRows.next()) {
            User user = new User();
            user.setId(id);
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Set<User> getAllUsers() {
        Set<User> users = new HashSet<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (userRows.next()) {
            User user = new User();
            user.setId(userRows.getLong("id"));
            user.setName(userRows.getString("name"));
            user.setEmail(userRows.getString("email"));
            users.add(user);
        }
        return users;
    }

    @Override
    public void removeUserById(Long id) {
        jdbcTemplate.update("DELETE * FROM users WHERE id = ?", id);
    }

    @Override
    public void removeAllUsers() {
        jdbcTemplate.update("DELETE * FROM users");
    }
}