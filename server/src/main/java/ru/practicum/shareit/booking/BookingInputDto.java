package ru.practicum.shareit.booking;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookingInputDto {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
