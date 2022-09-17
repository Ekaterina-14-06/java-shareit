package ru.practicum.shareit.reviews;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", schema = "public")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long reviewId;

    @Column(name = "description")
    private String description;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "review_date")
    private LocalDateTime date;

    @Column(name = "evaluation")
    private Boolean evaluation;

    @Column(name = "booking_id")
    private Long bookingId;
}
