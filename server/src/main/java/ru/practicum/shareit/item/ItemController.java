package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER = "X-Sharer-User-Id";
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(OWNER) Long ownerId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @ResponseBody
    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) Long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(OWNER) Long ownerId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(required = false) Integer size) {
        return itemService.getItemsByOwner(ownerId, from, size);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                          @RequestHeader(OWNER) Long ownerId) {
        return itemService.update(itemDto, ownerId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId, @RequestHeader(OWNER) Long ownerId) {
        itemService.delete(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchQuery(@RequestParam String text,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(required = false) Integer size) {
        return itemService.getItemsBySearchQuery(text, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ReviewDto createComment(@RequestBody ReviewDto reviewDto, @RequestHeader(OWNER) Long userId,
                                   @PathVariable Long itemId) {
        return itemService.createComment(reviewDto, itemId, userId);
    }
}
