package ru.practicum.shareit.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    @NotBlank
    private String description;

    @NotBlank
    private LocalDateTime created;
}