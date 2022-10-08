package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import ru.practicum.shareit.booking.BookingShortDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Value
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    @JsonIgnore
    User owner;
    Long requestId;
    BookingShortDto lastBooking;
    BookingShortDto nextBooking;
    List<ReviewDto> comments;
}
