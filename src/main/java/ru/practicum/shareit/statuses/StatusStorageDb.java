package ru.practicum.shareit.statuses;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StatusStorageDb implements StatusStorage {
    private final JdbcTemplate jdbcTemplate;

    public StatusStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public Status createStatus(Status status) {
        jdbcTemplate.update("INSERT INTO statuses (name, description) VALUES (?, ?)",
                status.getName(), status.getDescription());
        return status;
    }

    @Override
    public Status updateStatus(Status status) {
        jdbcTemplate.update("UPDATE statuses SET name = ?, description = ? WHERE id = ?",
                status.getName(), status.getDescription(), status.getId());
        return status;
    }

    @Override
    public Status getStatusById(Long id) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT * FROM statuses WHERE id = ?", id);
        if (statusRows.next()) {
            Status status = new Status();
            status.setId(id);
            status.setName(statusRows.getString("name"));
            status.setDescription(statusRows.getString("description"));
            return status;
        } else {
            return null;
        }
    }

    @Override
    public Set<Status> getAllStatuses() {
        Set<Status> statuses = new HashSet<>();
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT * FROM statuses");
        while (statusRows.next()) {
            Status status = new Status();
            status.setId(statusRows.getLong("id"));
            status.setName(statusRows.getString("name"));
            status.setDescription(statusRows.getString("description"));
            statuses.add(status);
        }
        return statuses;
    }

    @Override
    public void removeStatusById(Long id) {
        jdbcTemplate.update("DELETE * FROM statuses WHERE id = ?", id);
    }

    @Override
    public void removeAllStatuses() {
        jdbcTemplate.update("DELETE * FROM statuses");
    }
}
