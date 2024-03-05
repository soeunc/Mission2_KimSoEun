package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Goods;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {
    private String name;
    private String image;
    private String description;
    private String price;
    private Integer stock;

    public static GoodsDto fromEntity(Goods entity) {
        return GoodsDto.builder()
                .name(entity.getName())
                .image(entity.getImage())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }
}
