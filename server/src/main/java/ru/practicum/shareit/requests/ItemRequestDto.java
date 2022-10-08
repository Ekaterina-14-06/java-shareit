package ru.practicum.shareit.requests;

import lombok.Value;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class ItemRequestDto {
    Long id;
    String description;
    UserDto requestor;
    LocalDateTime created;
    List<ItemDto> items;
}
