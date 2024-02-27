package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.OrderOffer;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String sellerName;
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String titleImage;
    @Setter
    private Integer minPrice;
    private String status;
    @Setter
    private String response;
    private OrderOffer offer;

    // 사용자 정보는 제공 x
    public static ItemDto fromEntity(Item entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .sellerName(entity.getSellerName())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .titleImage(entity.getTitleImage())
                .minPrice(entity.getMinPrice())
                .status(entity.getStatus())
                .response(entity.getResponse())
                .build();
    }
}
