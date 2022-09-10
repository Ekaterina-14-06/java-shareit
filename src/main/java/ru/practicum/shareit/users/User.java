package ru.practicum.shareit.users;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users", schema = "public")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long userId;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "email")
    @Email
    @NotBlank
    @UniqueElements
    private String email;
}
