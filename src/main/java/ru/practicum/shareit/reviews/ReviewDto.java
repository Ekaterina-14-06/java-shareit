package ru.practicum.shareit.reviews;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    @NotBlank
    private String description;

    @NotBlank
    private Long itemId;

    @NotBlank
    private LocalDateTime date;

    @NotBlank
    private Boolean evaluation;

    private Long bookingId;
}
