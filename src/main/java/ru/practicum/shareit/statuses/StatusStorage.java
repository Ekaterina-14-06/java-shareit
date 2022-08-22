package ru.practicum.shareit.statuses;

import java.util.Set;

public interface StatusStorage {
    Status createStatus(Status status);

    Status updateStatus(Status status);

    Status getStatusById(Long id);

    Set<Status> getAllStatuses();

    void removeStatusById(Long id);

    void removeAllStatuses();
}
