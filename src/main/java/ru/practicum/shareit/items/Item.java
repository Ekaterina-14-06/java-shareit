package ru.practicum.shareit.items;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;

@Data
public class Item {
    @NotBlank
    @UniqueElements
    private Long id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private Boolean available;
    @NotBlank
    private Long owner;
    private Long request;
}
