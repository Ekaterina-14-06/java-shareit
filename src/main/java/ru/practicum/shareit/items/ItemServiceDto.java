package ru.practicum.shareit.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.users.User;
import ru.practicum.shareit.users.UserDto;
import ru.practicum.shareit.users.UserServiceDto;
import ru.practicum.shareit.users.UserStorageInMemory;

@Service
public class ItemServiceDto {
    private final ItemStorageInMemory itemStorageInMemory;
    private final UserStorageInMemory userStorageInMemory;
    private final ItemServiceImpl itemServiceImpl;
    private final UserServiceDto userServiceDto;

    @Autowired
    public ItemServiceDto(ItemStorageInMemory itemStorageInMemory,
                          UserStorageInMemory userStorageInMemory,
                          ItemServiceImpl itemServiceImpl,
                          UserServiceDto userServiceDto) {
        this.itemStorageInMemory = itemStorageInMemory;
        this.userStorageInMemory = userStorageInMemory;
        this.itemServiceImpl = itemServiceImpl;
        this.userServiceDto = userServiceDto;
    }

    public ItemDto getItemById(Long id) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(itemStorageInMemory.getItemById(id).getName());
        itemDto.setDescription(itemStorageInMemory.getItemById(id).getDescription());
        itemDto.setAvailable(itemStorageInMemory.getItemById(id).getAvailable());
        return itemDto;
    }

    public UserDto getUserOfItem(Long id) {
        for (User user : userStorageInMemory.getAllUsers()) {
            if (user.getUserId() == itemServiceImpl.getItemById(id).getUserId()) {
                return userServiceDto.getUserById(id);
            }
        }
        return null;
    }
}
