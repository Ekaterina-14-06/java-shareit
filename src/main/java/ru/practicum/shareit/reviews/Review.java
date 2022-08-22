package ru.practicum.shareit.reviews;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class Review {
    @NotBlank
    @UniqueElements
    private Long id;
    private String description;
    private Long item;
    private Long reviewer;
    private LocalDateTime date;
    private Boolean evaluation; // отзыв-комментарий: положительный (true) или отрицательный (false)
    private Long booking;
}
