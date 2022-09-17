package ru.practicum.shareit.statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.BookingDto;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/statuses")
public class StatusController {
    private final StatusServiceImpl statusServiceImpl;
    private final StatusServiceDto statusServiceDto;

    @Autowired
    public StatusController(StatusServiceImpl statusServiceImpl,
                            StatusServiceDto statusServiceDto) {
        this.statusServiceImpl = statusServiceImpl;
        this.statusServiceDto = statusServiceDto;
    }

    @PostMapping()
    public void addStatus(@Valid @RequestBody Status status) {
        statusServiceImpl.createStatus(status);
    }

    @PutMapping()
    public void updateStatus(@Valid @RequestBody Status status) {
        statusServiceImpl.updateStatus(status);
    }

    @DeleteMapping("/{id}")
    public void removeStatusById(@PathVariable("id") Long id) {
        statusServiceImpl.removeStatusById(id);
    }

    @DeleteMapping()
    public void removeAllStatuses() {
        statusServiceImpl.removeAllStatuses();
    }

    @GetMapping("/{id}")
    public StatusDto getStatusById(@PathVariable("id") Long id) {
        return statusServiceDto.getStatusById(id);
    }

    @GetMapping()
    public Set<StatusDto> getAllStatuses() {
        return statusServiceDto.getAllStatuses();
    }

    @GetMapping("/{id}/bookings")
    public Set<BookingDto> getBookingsOfStatus(@PathVariable("id") Long id) {
        return statusServiceDto.getBookingsOfStatus(id);
    }

    @DeleteMapping("/{id}/bookings")
    public void removeBookingsOfStatus(@PathVariable("id") Long id) {
        statusServiceImpl.removeBookingsOfStatus(id);
    }
}
