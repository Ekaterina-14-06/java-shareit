package ru.practicum.shareit.request.dto;

import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class ItemRequestDto {
    Long id;
    @NotBlank
    String description;
    UserDto requestor;
    LocalDateTime created;
    List<ItemDto> items;
}
