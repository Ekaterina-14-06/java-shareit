package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.users.User;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public BookingController(BookingServiceImpl bookingServiceImpl) {
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @PostMapping()
    public void addBooking(@Valid @RequestBody Booking booking) {
        bookingServiceImpl.createBooking(booking);
    }

    @PutMapping()
    public void updateBooking(@Valid @RequestBody Booking booking) {
        bookingServiceImpl.updateBooking(booking);
    }

    @DeleteMapping("/{id}")
    public void removeBookingById(@PathVariable("id") Long id) {
        bookingServiceImpl.removeBookingById(id);
    }

    @DeleteMapping()
    public void removeAllBookings() {
        bookingServiceImpl.removeAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable("id") Long id) {
        return bookingServiceImpl.getBookingById(id);
    }

    @GetMapping()
    public Set<Booking> getAllBookings() {
        return bookingServiceImpl.getAllBookings();
    }

    @GetMapping("/{id}/items")
    public Item getItemOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getItemOfBooking(id);
    }

    @GetMapping("/{id}/users")
    public User getUserOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getUserOfBooking(id);
    }

    @GetMapping("/{id}/statuses")
    public Status getStatusOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getStatusOfBooking(id);
    }

    @GetMapping("/{id}/reviews")
    public Review getReviewOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getReviewOfBooking(id);
    }
}
