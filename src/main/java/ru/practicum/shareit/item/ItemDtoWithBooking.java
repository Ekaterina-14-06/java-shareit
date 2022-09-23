package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDtoForItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithBooking {

    private Long id;
    @NotBlank(message = "name should not be blank")
    @NotNull
    private String name;
    @NotNull
    @NotBlank(message = "name should not be blank")
    private String description;
    @NotNull
    private Boolean available;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private List<ReviewDto> reviews;
}
