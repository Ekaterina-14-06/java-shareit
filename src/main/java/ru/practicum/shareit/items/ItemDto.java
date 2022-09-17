package ru.practicum.shareit.items;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemDto {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private Boolean available;
}
