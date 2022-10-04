package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.util.CheckServices;

@Component
public class ItemMapper {

    private CheckServices checker;

    @Autowired
    @Lazy
    public ItemMapper(CheckServices checkServices) {
        this.checker = checkServices;
    }

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != null ? item.getRequestId() : null,
                null,
                null,
                checker.getCommentsByItemId(item.getId()));
    }

    public ItemDto toItemExtDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != null ? item.getRequestId() : null,
                checker.getLastBooking(item.getId()),
                checker.getNextBooking(item.getId()),
                checker.getCommentsByItemId(item.getId()));
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                checker.findUserById(ownerId),
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
        );
    }

    public ReviewDto toCommentDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getText(),
                review.getItem(),
                review.getAuthor().getName(),
                review.getCreated());
    }
}