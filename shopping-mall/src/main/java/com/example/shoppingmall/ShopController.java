package com.example.shoppingmall;

import com.example.shoppingmall.dto.ShopDto;
import com.example.shoppingmall.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @PostMapping("/request-open")
    public ShopDto requestOpen(
            @RequestBody ShopDto dto
    ) {
        return service.requestOpen(dto);
    }

    @PutMapping("/{shopId}/update")
    public ShopDto updateShop(
            @PathVariable("shopId") Long id,
            @RequestBody ShopDto dto
    ) {
        return service.updateShop(id, dto);
    }

    @GetMapping("/sub-list")
    public List<ShopDto> shopList() {
        return service.shopList();
    }

    @PostMapping("/{shopId}/refusal")
    public ShopDto refusal(
            @PathVariable("shopId") Long id,
            @RequestBody ShopDto dto
    ) {
        return service.refusal(id, dto);
    }

    @PostMapping("/{shopId}/request-delete")
    public ShopDto requestDel(
            @PathVariable("shopId") Long id,
            @RequestBody ShopDto dto
    ) {
        return service.requestDel(id, dto);
    }

    @DeleteMapping("/{shopId}/delete")
    public void deleteShop(
            @PathVariable("shopId") Long id
    ) {
        service.deleteShop(id);
    }

}
