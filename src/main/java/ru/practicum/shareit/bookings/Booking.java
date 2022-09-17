package ru.practicum.shareit.bookings;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long bookingId;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    @NotBlank
    private Long userId;

    @Column(name = "status_id")
    private Long statusId;
}


