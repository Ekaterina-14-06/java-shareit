package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class ItemRequestStorageInMemory implements ItemRequestStorage {
    private final Set<ItemRequest> itemRequests = new HashSet<>();

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        itemRequests.add(itemRequest);
        log.info("Добавлен запрос {}", itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        try {
            boolean isPresent = false;
            for (ItemRequest itemRequestInItemRequests : itemRequests) {
                if (itemRequestInItemRequests.getItemRequestId() == itemRequest.getItemRequestId()) {
                    isPresent = true;
                    itemRequestInItemRequests.setDescription(itemRequest.getDescription());
                    itemRequestInItemRequests.setUserId(itemRequest.getUserId());
                    itemRequestInItemRequests.setCreated(itemRequest.getCreated());
                    log.info("Обновлён запрос {}", itemRequest);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменения значения полей несуществующего запроса (нет совпадений по id {}).",
                        itemRequest.getItemRequestId());
                throw new ValidationException("Запрос с таким id не существует (нечего обновлять). " +
                        "Значения полей запроса не были обновлены.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return itemRequest;
    }

    @Override
    public ItemRequest getItemRequestById(Long id) {
        try {
            for (ItemRequest itemRequest : itemRequests) {
                if (itemRequest.getItemRequestId() == id) {
                    return itemRequest;
                }
            }

            log.error("Попытка получения данных несуществующего запроса (нет совпадений по id ).");
            throw new ValidationException("Запрос с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Set<ItemRequest> getAllItemRequests() {
        return itemRequests;
    }

    @Override
    public void removeItemRequestById(Long id) {
        try {
            for (ItemRequest itemRequest : itemRequests) {
                if (itemRequest.getItemRequestId() == id) {
                    itemRequests.remove(itemRequest);
                    break;
                }
            }

            log.error("Попытка удаления несуществующего запроса (нет совпадений по id ).");
            throw new ValidationException("Запрос с таким id не существует." +
                    "Запись о запросе не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllItemRequests() {
        itemRequests.clear();
    }
}
