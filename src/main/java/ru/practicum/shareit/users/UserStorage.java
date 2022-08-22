package ru.practicum.shareit.users;

import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Set<User> getAllUsers();

    void removeUserById(Long id);

    void removeAllUsers();
}
