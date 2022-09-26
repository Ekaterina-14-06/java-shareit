package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoWithBooking> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "0") @Min(0) int from,
                                            @RequestParam(defaultValue = "20") @Positive int size) {
        return itemService.findAll(userId, from, size);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.save(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(itemDto, userId, id);
    }

    @PostMapping("/{itemId}/review")
    public ReviewDto createReview(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody ReviewDto reviewDto,
                                    @PathVariable long itemId) {
        return itemService.saveReview(userId, itemId, reviewDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBooking findItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findById(itemId, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable long id) {
        itemService.deleteById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestParam String text,
                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                        @RequestParam(defaultValue = "20") @Positive int size) {
        return itemService.searchItem(text, from, size);
    }
}
