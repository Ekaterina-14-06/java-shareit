package ru.practicum.shareit.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class UserStorageInMemory implements UserStorage {
    private final Set<User> users = new HashSet<>();


    @Override
    public User createUser(User user) {
        users.add(user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        try {
            boolean isPresent = false;
            for (User userInUsers : users) {
                if (userInUsers.getUserId() == user.getUserId()) {
                    isPresent = true;
                    userInUsers.setName(user.getName());
                    userInUsers.setEmail(user.getEmail());
                    log.info("Обновлён пользователь {}", user);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить свойства несуществующего пользователя (нет совпадений по id {}).",
                        user.getUserId());
                throw new ValidationException("Пользователя с таким id не существует (некого обновлять). " +
                        "Запись о пользователе не была обновлена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        try {
            for (User user : users) {
                if (user.getUserId() == id) {
                    return user;
                }
            }

            log.error("Попытка получения несуществующего пользователя (нет совпадений по id ).");
            throw new ValidationException("Пользователя с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<User> getAllUsers() {
        return users;
    }

    @Override
    public void removeUserById(Long id) {
        try {
            for (User user : users) {
                if (user.getUserId() == id) {
                    users.remove(user);
                    break;
                }
            }

            log.error("Попытка удаления несуществующего пользователя (нет совпадений по id ).");
            throw new ValidationException("Пользователя с таким id не существует." +
                    "Запись о пользователе не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllUsers() {
        users.clear();
    }
}