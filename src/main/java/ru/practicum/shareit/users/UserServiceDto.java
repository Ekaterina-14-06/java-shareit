package ru.practicum.shareit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceDto {
    private final UserStorageInMemory userStorageInMemory;

    @Autowired
    public UserServiceDto(UserStorageInMemory userStorageInMemory) {
        this.userStorageInMemory = userStorageInMemory;
    }

    public UserDto getUserById(Long id) {
        UserDto userDto = new UserDto();
        userDto.setName(userStorageInMemory.getUserById(id).getName());
        userDto.setEmail(userStorageInMemory.getUserById(id).getEmail());
        return userDto;
    }

    public Set<UserDto> getAllUserDtos() {
        return userStorageInMemory.getAllUserDtos();
    }
}
