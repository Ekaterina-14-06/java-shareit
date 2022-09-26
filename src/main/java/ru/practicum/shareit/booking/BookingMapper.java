package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getBookingId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus());
    }

    public Booking fromSimpleToBooking(BookingDtoSimple bookingDtoSimple) {
        return new Booking(bookingDtoSimple.getId(),
                bookingDtoSimple.getStart(),
                bookingDtoSimple.getEnd(),
                null,
                null,
                Status.WAITING);
    }

    public BookingDtoForItem toBookingDtoForItem(Booking booking) {
        return new BookingDtoForItem(booking.getBookingId(),
                booking.getBooker().getId()
        );
    }
}
