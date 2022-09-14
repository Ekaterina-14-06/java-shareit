package ru.practicum.shareit.statuses;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "statuses", schema = "public")
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long statusId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
