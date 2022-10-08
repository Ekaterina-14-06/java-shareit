package ru.practicum.shareit.booking;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookingShortDto {
    Long id;
    Long bookerId;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
