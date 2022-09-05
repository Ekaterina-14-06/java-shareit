package ru.practicum.shareit.reviews;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", schema = "public")
@Data
public class Review {  // class Comment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank
    @UniqueElements
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "item")
    private Long item;

    @Column(name = "reviewer")
    private Long reviewer;

    @Column(name = "review_date")
    private LocalDateTime date;

    @Column(name = "evaluation")
    private Boolean evaluation; // отзыв-комментарий: положительный (true) или отрицательный (false)

    @Column(name = "booking")
    private Long booking;
}
