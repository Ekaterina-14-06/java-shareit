package ru.practicum.shareit.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.items.ItemStorageInMemory;

import java.util.Optional;
import java.util.Set;

@Service
public class BookingServiceDto {
    private final BookingStorageInMemory bookingStorageInMemory;
    private final ItemStorageInMemory itemStorageInMemory;

    @Autowired
    public BookingServiceDto(BookingStorageInMemory bookingStorageInMemory, ItemStorageInMemory itemStorageInMemory) {
        this.bookingStorageInMemory = bookingStorageInMemory;
        this.itemStorageInMemory = itemStorageInMemory;
    }

    public Optional<BookingDto> getBookingById(Long id, Long userId) {
        if (bookingStorageInMemory.getBookingById(id).getUserId() == userId ||
                itemStorageInMemory.getItemById(bookingStorageInMemory.getBookingById(id).getItemId()).getUserId() == userId) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setStart(bookingStorageInMemory.getBookingById(id).getStart());
            bookingDto.setEnd(bookingStorageInMemory.getBookingById(id).getEnd());
            bookingDto.setItemId(bookingStorageInMemory.getBookingById(id).getItemId());
            bookingDto.setStatusId(bookingStorageInMemory.getBookingById(id).getStatusId());
            return Optional.of(bookingDto);
        }
        return Optional.empty();
    }

    public Set<BookingDto> getAllBookingDtos() {
        return bookingStorageInMemory.getAllBookingDtos();
    }
}
