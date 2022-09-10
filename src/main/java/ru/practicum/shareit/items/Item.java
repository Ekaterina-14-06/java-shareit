package ru.practicum.shareit.items;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "items", schema = "public")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long itemId;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    @NotBlank
    private Boolean available;

    @Column(name = "user_id")
    @NotBlank
    private Long userId;

    @Column(name = "request_id")
    private Long requestId;
}
