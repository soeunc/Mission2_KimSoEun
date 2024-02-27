package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.OrderOffer;
import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderOfferDto {
    private Long id;
    private String itemName;
    private String offerName;
    private UserEntity user;

    public static OrderOfferDto fromEntity(OrderOffer entity) {
        return OrderOfferDto.builder()
                .id(entity.getId())
                .itemName(entity.getItemName())
                .offerName(entity.getOfferName())
                .build();
    }
}
