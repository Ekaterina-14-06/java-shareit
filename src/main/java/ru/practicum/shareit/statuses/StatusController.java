package ru.practicum.shareit.statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookings.Booking;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(path = "/statuses")
public class StatusController {
    private final StatusServiceImpl statusServiceImpl;

    @Autowired
    public StatusController(StatusServiceImpl statusServiceImpl) {
        this.statusServiceImpl = statusServiceImpl;
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
    public Status getStatusById(@PathVariable("id") Long id) {
        return statusServiceImpl.getStatusById(id);
    }

    @GetMapping()
    public Set<Status> getAllStatuses() {
        return statusServiceImpl.getAllStatuses();
    }

    @GetMapping("/{id}/bookings")
    public Set<Booking> getBookingsOfStatus(@PathVariable("id") Long id) {
        return statusServiceImpl.getBookingsOfStatus(id);
    }

    @DeleteMapping("/{id}/bookings")
    public void removeBookingsOfStatus(@PathVariable("id") Long id) {
        statusServiceImpl.removeBookingsOfStatus(id);
    }
}
