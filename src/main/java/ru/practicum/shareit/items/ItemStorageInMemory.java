package ru.practicum.shareit.items;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class ItemStorageInMemory implements ItemStorage {
    private final Set<Item> items = new HashSet<>();

    @Override
    public Item createItem(Item item) {
        items.add(item);
        log.info("Добавлена вещь {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        try {
            boolean isPresent = false;
            for (Item itemInItems : items) {
                if (itemInItems.getItemId() == item.getItemId()) {
                    isPresent = true;
                    itemInItems.setName(item.getName());
                    itemInItems.setDescription(item.getDescription());
                    itemInItems.setUserId(item.getUserId());
                    itemInItems.setAvailable(item.getAvailable());
                    itemInItems.setRequestId(item.getRequestId());
                    log.info("Обновлены значения полей вещи {}", item);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменения значения полей несуществующей вещи (нет совпадений по id {}).",
                        item.getItemId());
                throw new ValidationException("Вещи с таким id не существует (нечего обновлять). " +
                        "Значения полей вещи не были обновлены.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return item;
    }

    @Override
    public Item getItemById(Long id) {
        try {
            for (Item item : items) {
                if (item.getItemId() == id) {
                    return item;
                }
            }

            log.error("Попытка получения данных несуществующей вещи (нет совпадений по id ).");
            throw new ValidationException("вещь с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Set<Item> getAllItems() {
        return items;
    }

    @Override
    public void removeItemById(Long id) {
        try {
            for (Item item : items) {
                if (item.getItemId() == id) {
                    items.remove(item);
                    break;
                }
            }

            log.error("Попытка удаления несуществующей вещи (нет совпадений по id ).");
            throw new ValidationException("Вещь с таким id не существует." +
                    "Запись о вещи не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllItems() {
        items.clear();
    }
}
