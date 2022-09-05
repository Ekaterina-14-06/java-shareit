package ru.practicum.shareit.statuses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class StatusStorageInMemory implements StatusStorage {
    private final Set<Status> statuses = new HashSet<>();

    @Override
    public Status createStatus(Status status) {
        statuses.add(status);
        log.info("Добавлен статус {}", status);
        return status;
    }

    @Override
    public Status updateStatus(Status status) {
        try {
            boolean isPresent = false;
            for (Status statusInStatuses : statuses) {
                if (statusInStatuses.getId() == status.getId()) {
                    isPresent = true;
                    statusInStatuses.setName(status.getName());
                    statusInStatuses.setDescription(status.getDescription());
                    log.info("Обновлён статус {}", status);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить значения полей несуществующего статуса (нет совпадений по id {}).", status.getId());
                throw new ValidationException("Статуса с таким id не существует (нечего обновлять). " +
                        "Значения полей статуса не были обновлены.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }

    @Override
    public Status getStatusById(Long id) {
        try {
            for (Status status : statuses) {
                if (status.getId() == id) {
                    return status;
                }
            }

            log.error("Попытка получения данных несуществующего статуса (нет совпадений по id ).");
            throw new ValidationException("Статуса с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Long getStatusIdByName (String name) {
        try {
            for (Status status : statuses) {
                if (status.getName().equals(name)) {
                    return status.getId();
                }
            }

            log.error("Попытка получения данных несуществующего статуса (нет совпадений по имени ).");
            throw new ValidationException("Статуса с таким именем не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<Status> getAllStatuses() {
        return statuses;
    }

    @Override
    public void removeStatusById(Long id) {
        try {
            for (Status status : statuses) {
                if (status.getId() == id) {
                    statuses.remove(status);
                    break;
                }
            }

            log.error("Попытка удаления несуществующего статуса (нет совпадений по id ).");
            throw new ValidationException("Статуса с таким id не существует." +
                    "Запись о статусе не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllStatuses() {
        statuses.clear();
    }
}
