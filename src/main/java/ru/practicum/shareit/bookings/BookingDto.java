package ru.practicum.shareit.bookings;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private LocalDateTime start;

    private LocalDateTime end;

    @NotBlank
    private Long itemId;

    @NotBlank
    private Long statusId;
}


