package ru.practicum.shareit.util;

import lombok.Getter;
import ru.practicum.shareit.exception.ValidationException;

@Getter
public class PaginableChecker {
    private Integer pageSize;
    private Integer index;
    private Integer totalPages;

    public PaginableChecker(Integer from, Integer size) {
        if (size != null) {
            if ((from < 0) || (size < 0)) {
                throw new ValidationException("Значение меньше нуля.");
            }
            if (size.equals(0)) {
                throw new ValidationException("Значение должно быть больше нуля.");
            }
        }
        pageSize = from;
        index = 1;
        totalPages = 0;
        if (size == null) {
            if (from.equals(0)) {
                pageSize = 1000;
                index = 0;
            }
        } else {
            if (from.equals(size)) {
                pageSize = size;
            }
            if (from.equals(0)) {
                pageSize = size;
                index = 0;
            }
            totalPages = index + 1;
            if ((from < size) && (!from.equals(0))) {
                totalPages = size / from + index;
                if (size % from != 0) {
                    totalPages++;
                }
            }
        }
    }
}
