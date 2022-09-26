package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                null,
                LocalDateTime.now());
    }

    public ItemRequestDtoWithItems toItemRequestDtoWithItems(ItemRequest itemRequest) {
        ItemRequestDtoWithItems itemRequestDtoWithItems = new ItemRequestDtoWithItems(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                new ArrayList<>()
        );
        List<ItemDto> items = itemRepository.findAllByItemRequestId(itemRequestDtoWithItems.getId())
                .stream().map(itemMapper::toItemDto).collect(Collectors.toList());
        if (!items.isEmpty()) {
            itemRequestDtoWithItems.setItems(items);
        }
        return itemRequestDtoWithItems;

    }

}
