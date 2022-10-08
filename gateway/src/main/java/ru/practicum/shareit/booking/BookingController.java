package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_ID) Long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from,
                                              @RequestParam(required = false) Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestParam(name = "state", defaultValue = "all")
                                                   String stateParam,
                                                   @RequestHeader(USER_ID) Long userId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(required = false) Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
       
        return bookingClient.getBookingsOwner(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) Long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        
        return bookingClient.create(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID) Long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable Long bookingId,
                                         @RequestHeader(USER_ID) Long userId, @RequestParam Boolean approved) {
       
        return bookingClient.update(bookingId, userId, approved);
    }
}
