package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BookingException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(long userId) {
        return userMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new BookingException("Пользователя с Id = " + userId + " нет в БД")));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        UserDto oldUserDto = findById(userId);
        if (userDto.getName() != null) {
            oldUserDto.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUserDto.setEmail(userDto.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(oldUserDto)));
    }

    @Override
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

}

