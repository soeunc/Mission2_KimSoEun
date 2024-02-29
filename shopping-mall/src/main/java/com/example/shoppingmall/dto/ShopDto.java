package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Enum.ShopCategory;
import com.example.shoppingmall.entity.Shop;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String shopName;
    private String introduction;
    private ShopCategory category;
    private String shopStatus;
    private String shopResponse;
    private String reason;
    private String deleteReason;

    public boolean condition =
            shopName != null && introduction != null && category != null;
    public static ShopDto fromEntity(Shop entity) {
        return ShopDto.builder()
                .id(entity.getId())
                .shopName(entity.getShopName())
                .introduction(entity.getIntroduction())
                .category(entity.getCategory())
                .shopStatus(entity.getShopStatus())
                .shopResponse(entity.getShopResponse())
                .reason(entity.getReason())
                .build();
    }
}
