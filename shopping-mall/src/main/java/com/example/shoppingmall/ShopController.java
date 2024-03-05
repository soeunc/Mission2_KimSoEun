package com.example.shoppingmall;

import com.example.shoppingmall.dto.GoodsDto;
import com.example.shoppingmall.dto.ShopDto;
import com.example.shoppingmall.entity.Enum.ShopCategory;
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

    @PostMapping("/{shopId}/create")
    public GoodsDto create(
            @PathVariable("shopId") Long id,
            @RequestBody GoodsDto dto
    ) {
        return service.create(id, dto);
    }

    @PutMapping("/{shopId}/{goodsId}/update")
    public GoodsDto update(
            @PathVariable("shopId") Long shopId,
            @PathVariable("goodsId") Long goodsId,
            @RequestBody GoodsDto dto
    ) {
        return service.update(shopId, goodsId, dto);
    }

    @DeleteMapping("/{shopId}/{goodsId}/delete")
    public void delete(
            @PathVariable("shopId") Long shopId,
            @PathVariable("goodsId") Long goodsId
    ) {
        service.delete(shopId, goodsId);
    }

    @GetMapping("/search")
    public List<ShopDto> searchShops(
            @RequestParam(name = "shopName", required = false) String shopName,
            @RequestParam(name = "category", required = false) ShopCategory category
    ) {
        return service.search(shopName, category);
    }
}
