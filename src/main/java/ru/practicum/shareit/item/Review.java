package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews", schema = "public")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @Column(name = "description", nullable = false)
    private String text;
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_Id")
    private Item item;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User author;
    @Column(name = "created")
    private LocalDateTime created;
}
