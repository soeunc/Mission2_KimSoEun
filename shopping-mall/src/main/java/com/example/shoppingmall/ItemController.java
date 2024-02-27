package com.example.shoppingmall;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.OrderOfferDto;
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

    @PostMapping("/create")
    public ItemDto create(
            @RequestBody ItemDto dto
    ) {
        return service.create(dto);
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

    @PutMapping("/{itemId}/update")
    public ItemDto update(
            @PathVariable("itemId") Long itemId,
            @RequestBody ItemDto dto
    ) {
        return service.update(itemId, dto);
    }

    @DeleteMapping("/{itemId}/delete")
    public void delete(
            @PathVariable("itemId") Long itemId
    ) {
        service.delete(itemId);
    }

    @PostMapping("/{itemId}/offers")
    public OrderOfferDto offer(
            @PathVariable("itemId") Long itemId
    ) {
        return service.offer(itemId);
    }

    @GetMapping("/offer/read-offer")
    public List<OrderOfferDto> readOffer() {
        return service.readOffer();
    }

    @GetMapping("/offer/read-seller")
    public List<OrderOfferDto> readSeller() {
        return service.readSeller();
    }

    @PutMapping("/response/{itemId}/{offerId}")
    public ItemDto updateResponse(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long offerId,
            @RequestParam String response
    ) {
        return service.updateResponse(itemId, offerId, response);
    }

    @PutMapping("/status/{itemId}")
    public ItemDto updateStatus(
            @PathVariable("itemId") Long itemId
    ) {
        return service.updateStatus(itemId);
    }
}
