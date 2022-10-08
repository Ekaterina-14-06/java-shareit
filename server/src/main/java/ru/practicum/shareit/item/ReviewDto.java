package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ReviewDto {
    Long id;
    String text;
    @JsonIgnore
    Item item;
    String authorName;
    LocalDateTime created;
}
