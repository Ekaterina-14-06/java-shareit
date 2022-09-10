package ru.practicum.shareit.users;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    @UniqueElements
    private String email;
}
