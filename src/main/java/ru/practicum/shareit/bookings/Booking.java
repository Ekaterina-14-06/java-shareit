package ru.practicum.shareit.bookings;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class Booking {
    @NotBlank
    @UniqueElements
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank
    private Long item;
    @NotBlank
    private Long booker;
    @NotBlank
    private Long status;
}


