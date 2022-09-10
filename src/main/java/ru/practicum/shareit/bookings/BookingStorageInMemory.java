package ru.practicum.shareit.bookings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class BookingStorageInMemory implements BookingStorage {
    private final Set<Booking> bookings = new HashSet<>();
    private final Set<BookingDto> bookingDtos = new HashSet<>();

    @Override
    public Booking createBooking(Booking booking) {
        bookings.add(booking);
        log.info("Добавлено бронирование {}", booking);
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        try {
            boolean isPresent = false;
            for (Booking bookingInBookings : bookings) {
                if (bookingInBookings.getBookingId() == booking.getBookingId()) {
                    isPresent = true;
                    bookingInBookings.setStart(booking.getStart());
                    bookingInBookings.setEnd(booking.getEnd());
                    bookingInBookings.setItemId(booking.getItemId());
                    bookingInBookings.setUserId(booking.getUserId());
                    bookingInBookings.setStatusId(booking.getStatusId());
                    log.info("Обновлено бронирование {}", booking);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить свойства несуществующего бронирования (нет совпадений по id {}).", booking.getBookingId());
                throw new ValidationException("Бронирования с таким id не существует (некого обновлять). " +
                        "Запись о бронировании не была обновлена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        try {
            for (Booking booking : bookings) {
                if (booking.getBookingId() == id) {
                    return booking;
                }
            }

            log.error("Попытка получения несуществующего бронирования (нет совпадений по id ).");
            throw new ValidationException("Бронирования с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Set<Booking> getAllBookings() {
        return bookings;
    }

    @Override
    public void removeBookingById(Long id) {
        try {
            for (Booking booking : bookings) {
                if (booking.getBookingId() == id) {
                    bookings.remove(booking);
                    break;
                }
            }

            log.error("Попытка удаления несуществующего бронирования (нет совпадений по id ).");
            throw new ValidationException("Бронирования с таким id не существует." +
                    "Запись о бронировании не была удалена.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllBookings() {
        bookings.clear();
    }

    public void changeStatus (Long bookingId, Long statusId) {
        try {
            boolean isPresent = false;
            for (Booking bookingInBookings : bookings) {
                if (bookingInBookings.getBookingId() == bookingId) {
                    isPresent = true;
                    bookingInBookings.setStatusId(statusId);
                    log.info("Обновлен статус");
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить статус несуществующего бронирования (нет совпадений по id {}).", bookingId);
                throw new ValidationException("Бронирования с таким id не существует (некого обновлять). " +
                        "Запись о бронировании не была обновлена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    public Set<BookingDto> getAllBookingDtos() {
        return bookingDtos;
    }
}
