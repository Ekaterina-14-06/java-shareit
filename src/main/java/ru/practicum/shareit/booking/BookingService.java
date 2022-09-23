package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingDtoSimple;

import java.util.List;

public interface BookingService {

    BookingDto findById(long bookingId, long userId);

    List<BookingDto> findAll(long userId, String state, int from, int size);

    BookingDto save(BookingDtoSimple bookingDtoSimple, long userId);

    BookingDto update(long bookingId, BookingDto bookingDto);

    void deleteById(long bookingId);

    BookingDto approve(long userId, long bookingId, Boolean approved);

    List<BookingDto> findAllByItemOwnerId(long userId, String state, int from, int size);

}
