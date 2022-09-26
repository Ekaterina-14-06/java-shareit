package ru.practicum.shareit.item;

import ru.practicum.shareit.item.ReviewDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {

    ItemDtoWithBooking findById(long itemId, long userId);

    List<ItemDtoWithBooking> findAll(long userId, int from, int size);

    ItemDto save(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId, long id);

    void deleteById(long itemId);

    List<ItemDto> searchItem(String text, int from, int size);

    ReviewDto saveReview(long userId, long itemId, ReviewDto reviewDto);
}
