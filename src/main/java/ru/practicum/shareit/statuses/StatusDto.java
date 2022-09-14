package ru.practicum.shareit.statuses;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;

@Data
public class StatusDto {
    @NotBlank
    @UniqueElements
    private String name;

    private String description;
}
