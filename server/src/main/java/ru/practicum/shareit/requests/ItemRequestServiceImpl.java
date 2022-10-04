package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.util.CheckServices;
import ru.practicum.shareit.util.PaginableChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final CheckServices checker;
    private final ItemRequestMapper mapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository repository,
                                  CheckServices checkServices, ItemRequestMapper mapper) {
        this.repository = repository;
        this.checker = checkServices;
        this.mapper = mapper;
    }

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requestorId, LocalDateTime created) {
        ItemRequest itemRequest = mapper.toItemRequest(itemRequestDto, requestorId, created);
        return mapper.toItemRequestDto(repository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        checker.isExistUser(userId);
        ItemRequest itemRequest = repository.findById(itemRequestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(itemRequestId + " not found!"));
        return mapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnItemRequests(Long requestorId) {
        checker.isExistUser(requestorId);
        return repository.findAllByRequestorId(requestorId,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(mapper::toItemRequestDto)
                .collect(toList());
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        checker.isExistUser(userId);
        List<ItemRequestDto> listItemRequestDto = new ArrayList<>();
        Pageable pageable;
        Page<ItemRequest> page;
        PaginableChecker pager = new PaginableChecker(from, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        if (size == null) {
            List<ItemRequest> listItemRequest = repository.findAllByRequestorIdNotOrderByCreatedDesc(userId);
            listItemRequestDto
                    .addAll(listItemRequest.stream().skip(from).map(mapper::toItemRequestDto).collect(toList()));
        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable =
                        PageRequest.of(i, pager.getPageSize(), sort);
                page = repository.findAllByRequestorIdNot(userId, pageable);
                listItemRequestDto.addAll(page.stream().map(mapper::toItemRequestDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listItemRequestDto = listItemRequestDto.stream().limit(size).collect(toList());
        }
        return listItemRequestDto;
    }
}
