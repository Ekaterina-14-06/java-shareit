package ru.practicum.shareit.bookings;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "items", schema = "public")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long id;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "item")
    @NotBlank
    private Long item;

    @Column(name = "booker")
    @NotBlank
    private Long booker;

    @Column(name = "status")
    @NotBlank
    private Long status;
}


