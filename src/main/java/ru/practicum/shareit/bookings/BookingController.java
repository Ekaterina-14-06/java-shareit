package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.Item;
import ru.practicum.shareit.reviews.Review;
import ru.practicum.shareit.statuses.Status;
import ru.practicum.shareit.users.User;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;
    private final BookingServiceDto bookingServiceDto;

    @Autowired
    public BookingController(BookingServiceImpl bookingServiceImpl,
                             BookingServiceDto bookingServiceDto) {
        this.bookingServiceImpl = bookingServiceImpl;
        this.bookingServiceDto = bookingServiceDto;
    }

    @PostMapping()
    public void addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody Booking booking) {
        bookingServiceImpl.createBooking(booking, userId);
    }

    @PutMapping()
    public void updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody Booking booking) {
        bookingServiceImpl.updateBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public void changeStatus (@PathVariable Long bookingId,
                              @RequestParam Boolean approved,
                              @RequestBody Booking booking,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingServiceImpl.changeStatus(bookingId, approved, booking.getBookingId(), userId);
    }

    @DeleteMapping("/{id}")
    public void removeBookingById(@PathVariable("id") Long id) {
        bookingServiceImpl.removeBookingById(id);
    }

    @DeleteMapping()
    public void removeAllBookings() {
        bookingServiceImpl.removeAllBookings();
    }

    @GetMapping("/{bookingId}")
    public Optional<BookingDto> getBookingById(@PathVariable("bookingId") Long id,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingServiceDto.getBookingById(id, userId);
    }

    @GetMapping()
    public Set<Booking> getBookingsOfUser(@RequestParam String state,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingServiceImpl.getBookingsOfUser(state, userId);
    }

    @GetMapping("/owner")
    public Set<Booking> getBookingsOfOwner(@RequestParam String state,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingServiceImpl.getBookingsOfOwner(state, userId);
    }

    @GetMapping()
    public Set<BookingDto> getAllBookingDtos() {
        return bookingServiceDto.getAllBookingDtos();
    }

    @GetMapping("/{id}/items")
    public Optional<Item> getItemOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getItemOfBooking(id);
    }

    @GetMapping("/{id}/users")
    public Optional<User> getUserOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getUserOfBooking(id);
    }

    @GetMapping("/{id}/statuses")
    public Optional<Status> getStatusOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getStatusOfBooking(id);
    }

    @GetMapping("/{id}/reviews")
    public Optional<Review> getReviewOfBooking(@PathVariable("id") Long id) {
        return bookingServiceImpl.getReviewOfBooking(id);
    }
}