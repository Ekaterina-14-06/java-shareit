package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.util.CheckServices;
import ru.practicum.shareit.util.PaginableChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;
    private final CheckServices checker;

    @Autowired
    @Lazy
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              CheckServices checkServices) {
        this.repository = bookingRepository;
        this.mapper = bookingMapper;
        this.checker = checkServices;
    }

    @Override
    public BookingDto create(BookingInputDto bookingInputDto, Long bookerId) {

        checker.isExistUser(bookerId);

        if (!checker.isAvailableItem(bookingInputDto.getItemId())) {
            throw new ValidationException("Ошибка бронирования");
        }
        Booking booking = mapper.toBooking(bookingInputDto, bookerId);
        if (bookerId.equals(booking.getItem().getOwner().getId())) {
            throw new BookingNotFoundException("Ошибка бронирования");
        }
        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        checker.isExistUser(userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Ошибка бронирования"));
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Ошибка бронирования");
        }

        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
            } else {
                throw new UserNotFoundException("Ошибка пользователя при бронировании!");
            }
        } else if ((checker.isItemOwner(booking.getItem().getId(), userId)) &&
                (!booking.getStatus().equals(Status.CANCELED))) {
            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("Бронирование уже существует");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("Бронирование было отменено!");
            }
        }

        return mapper.toBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        checker.isExistUser(userId);
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено!"));
        if (booking.getBooker().getId().equals(userId) || checker.isItemOwner(booking.getItem().getId(), userId)) {
            return mapper.toBookingDto(booking);
        } else {
            throw new UserNotFoundException("Ошибка бронирование пользователя.");
        }
    }

    @Override
    public List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size) {
        checker.isExistUser(userId);
        List<BookingDto> listBookingDto = new ArrayList<>();
        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        ;
        Page<Booking> page;
        PaginableChecker pager = new PaginableChecker(from, size);

        if (size == null) {
            pageable =
                    PageRequest.of(pager.getIndex(), pager.getPageSize(), sort);
            do {
                page = getPageBookings(state, userId, pageable);
                listBookingDto.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                pageable = pageable.next();
            } while (page.hasNext());

        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = getPageBookings(state, userId, pageable);
                listBookingDto.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listBookingDto = listBookingDto.stream().limit(size).collect(toList());
        }
        return listBookingDto;
    }

    private Page<Booking> getPageBookings(String state, Long userId, Pageable pageable) {
        Page<Booking> page;
        switch (state) {
            case "ALL":
                page = repository.findByBookerId(userId, pageable);
                break;
            case "CURRENT":
                page = repository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case "PAST":
                page = repository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable);
                break;
            case "FUTURE":
                page = repository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable);
                break;
            case "WAITING":
                page = repository.findByBookerIdAndStatus(userId, Status.WAITING, pageable);
                break;
            case "REJECTED":
                page = repository.findByBookerIdAndStatus(userId, Status.REJECTED, pageable);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return page;
    }

    @Override
    public List<BookingDto> getBookingsOwner(String state, Long userId, Integer from, Integer size) {
        checker.isExistUser(userId);
        List<BookingDto> listBookingDto = new ArrayList<>();
        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        ;
        Page<Booking> page;
        PaginableChecker pager = new PaginableChecker(from, size);

        if (size == null) {
            pageable =
                    PageRequest.of(pager.getIndex(), pager.getPageSize(), sort);
            do {
                page = getPageBookingsOwner(state, userId, pageable);
                listBookingDto.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                pageable = pageable.next();
            } while (page.hasNext());

        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = getPageBookingsOwner(state, userId, pageable);
                listBookingDto.addAll(page.stream().map(mapper::toBookingDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listBookingDto = listBookingDto.stream().limit(size).collect(toList());
        }
        return listBookingDto;
    }

    private Page<Booking> getPageBookingsOwner(String state, Long userId, Pageable pageable) {
        Page<Booking> page;
        switch (state) {
            case "ALL":
                page = repository.findByItemOwnerId(userId, pageable);
                break;
            case "CURRENT":
                page = repository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
                break;
            case "PAST":
                page = repository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable);
                break;
            case "WAITING":
                page = repository.findByItemOwnerIdAndStatus(userId, Status.WAITING, pageable);
                break;
            case "REJECTED":
                page = repository.findByItemOwnerIdAndStatus(userId, Status.REJECTED, pageable);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return page;
    }

    @Override
    public BookingShortDto getLastBooking(Long itemId) {
        BookingShortDto bookingShortDto =
                mapper.toBookingShortDto(repository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId,
                        LocalDateTime.now()));
        return bookingShortDto;
    }

    @Override
    public BookingShortDto getNextBooking(Long itemId) {
        BookingShortDto bookingShortDto =
                mapper.toBookingShortDto(repository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId,
                        LocalDateTime.now()));
        return bookingShortDto;
    }

    @Override
    public Booking getBookingWithUserBookedItem(Long itemId, Long userId) {
        return repository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), Status.APPROVED);
    }
}
