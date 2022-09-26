package ru.practicum.shareit.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(long userId);

    List<ItemRequest> findAll();
}
