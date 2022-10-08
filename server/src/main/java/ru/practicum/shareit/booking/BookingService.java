package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingInputDto bookingDto, Long bookerId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size);

    List<BookingDto> getBookingsOwner(String state, Long userId, Integer from, Integer size);

    BookingShortDto getLastBooking(Long itemId);

    BookingShortDto getNextBooking(Long itemId);

    Booking getBookingWithUserBookedItem(Long itemId, Long userId);

}
