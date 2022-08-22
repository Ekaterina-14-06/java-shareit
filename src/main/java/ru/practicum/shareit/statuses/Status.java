package ru.practicum.shareit.statuses;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;

@Data
public class Status {
    @NotBlank
    @UniqueElements
    private Long id;
    @NotBlank
    @UniqueElements
    private String name;
    private String description;
}
