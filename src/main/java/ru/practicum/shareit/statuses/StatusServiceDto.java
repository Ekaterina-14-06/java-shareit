package ru.practicum.shareit.statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.bookings.Booking;
import ru.practicum.shareit.bookings.BookingDto;
import ru.practicum.shareit.bookings.BookingStorageInMemory;

import java.util.HashSet;
import java.util.Set;

@Service
public class StatusServiceDto {
    private final StatusStorageInMemory statusStorageInMemory;
    private final BookingStorageInMemory bookingStorageInMemory;

    @Autowired
    public StatusServiceDto(StatusStorageInMemory statusStorageInMemory,
                            BookingStorageInMemory bookingStorageInMemory) {
        this.statusStorageInMemory = statusStorageInMemory;
        this.bookingStorageInMemory = bookingStorageInMemory;
    }

    public StatusDto getStatusById(Long id) {
        StatusDto statusDto = new StatusDto();
        statusDto.setName(statusStorageInMemory.getStatusById(id).getName());
        statusDto.setDescription(statusStorageInMemory.getStatusById(id).getDescription());
        return statusDto;
    }

    public Set<StatusDto> getAllStatuses() {
        Set<StatusDto> statusDtos = new HashSet<>();
        for (Status status : statusStorageInMemory.getAllStatuses()) {
            StatusDto statusDto = new StatusDto();
            statusDto.setName(status.getName());
            statusDto.setDescription(status.getDescription());
            statusDtos.add(statusDto);
        }
        return statusDtos;
    }

    public Set<BookingDto> getBookingsOfStatus(Long id) {
        Set<BookingDto> bookingsOfStatus = new HashSet<>();
        for (Booking booking : bookingStorageInMemory.getAllBookings()) {
            if (booking.getStatusId() == id) {
                BookingDto bookingDto = new BookingDto();
                bookingDto.setStart(booking.getStart());
                bookingDto.setEnd(booking.getEnd());
                bookingDto.setItemId(booking.getItemId());
                bookingDto.setStatusId(booking.getStatusId());
                bookingsOfStatus.add(bookingDto);
            }
        }
        return bookingsOfStatus;
    }
}
