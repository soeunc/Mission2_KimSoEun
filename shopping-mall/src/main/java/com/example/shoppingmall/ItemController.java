package com.example.shoppingmall;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping("/{userId}/create")
    public ItemDto create(
            @PathVariable("userId") Long userId,
            @RequestBody ItemDto dto
    ) {
        return service.create(userId, dto);
    }

    @GetMapping
    public List<ItemDto> readAll() {
        return service.readAll();
    }

    @GetMapping("/{itemId}")
    public ItemDto readOne(
            @PathVariable("itemId") Long itemId
    ) {
        return service.readOne(itemId);
    }

    @PutMapping("/{userId}/{itemId}/update")
    public ItemDto update(
            @PathVariable("userId") Long userId,
            @PathVariable("itemId") Long itemId,
            @RequestBody ItemDto dto
    ) {
        return service.update(userId, itemId, dto);
    }

    @DeleteMapping("/{userId}/{itemId}/delete")
    public void delete(
            @PathVariable("userId") Long userId,
            @PathVariable("itemId") Long itemId
    ) {
        service.delete(userId, itemId);
    }
}
