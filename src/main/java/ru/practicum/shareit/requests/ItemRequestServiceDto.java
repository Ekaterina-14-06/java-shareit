package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.items.ItemDto;
import ru.practicum.shareit.items.ItemStorageInMemory;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;
import ru.practicum.shareit.users.UserStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class ItemRequestServiceDto {
    private final ItemRequestStorageInMemory itemRequestStorageInMemory;
    private final ItemRequestServiceImpl itemRequestServiceImple;
    private final ItemStorageInMemory itemStorageInMemory;
    private final UserStorageInMemory userStorageInMemory;

    @Autowired
    public ItemRequestServiceDto(ItemRequestStorageInMemory itemRequestStorageInMemory,
                                 ItemRequestServiceImpl itemRequestServiceImple,
                                 ItemStorageInMemory itemStorageInMemory,
                                 UserStorageInMemory userStorageInMemory) {
        this.itemRequestStorageInMemory = itemRequestStorageInMemory;
        this.itemRequestServiceImple = itemRequestServiceImple;
        this.itemStorageInMemory = itemStorageInMemory;
        this.userStorageInMemory = userStorageInMemory;
    }

    public ItemRequestDto getItemRequestById(Long id) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(itemRequestStorageInMemory.getItemRequestById(id).getDescription());
        itemRequestDto.setCreated(itemRequestStorageInMemory.getItemRequestById(id).getCreated());
        return itemRequestDto;
    }

    public Set<ItemRequestDto> getAllItemRequests() {
        Set<ItemRequestDto> itemRequestDtos = new HashSet<>();
        for (ItemRequest itemRequest : itemRequestStorageInMemory.getAllItemRequests()) {
            ItemRequestDto itemRequestDto = new ItemRequestDto();
            itemRequestDto.setDescription(itemRequest.getDescription());
            itemRequestDto.setCreated(itemRequest.getCreated());
            itemRequestDtos.add(itemRequestDto);
        }
        return itemRequestDtos;
    }

    public UserDto getUserOfItemRequest(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == itemRequestServiceImple.getItemRequestById(id).getUserId()) {
                UserDto userDto = new UserDto();
                userDto.setName(user.getName());
                userDto.setEmail(user.getEmail());
                return userDto;
            }
        }
        return null;
    }

    public Set<ItemDto> getItemsOfItemRequest(Long id) {
        Set<ItemDto> itemsOfItemRequest = new HashSet<>();
        for (Item item : itemStorageInMemory.getAllItems()) {
            if (item.getRequestId() == itemRequestServiceImple.getItemRequestById(id).getItemRequestId()) {
                ItemDto itemDto = new ItemDto();
                itemDto.setName(item.getName());
                itemDto.setDescription(item.getDescription());
                itemDto.setAvailable(item.getAvailable());
                itemsOfItemRequest.add(itemDto);
            }
        }
        return itemsOfItemRequest;
    }
}
