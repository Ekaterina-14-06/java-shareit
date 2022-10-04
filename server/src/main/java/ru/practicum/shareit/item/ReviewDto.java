package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String text;
    @JsonIgnore
    private Item item;
    private String authorName;
    private LocalDateTime created;
}
