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
    // 쇼핑몰 오픈 상태
    private String shopStatus;
    // 쇼핑몰 개설 응답
    private String shopResponse;
    // 개설 불가 이유
    private String reason;
    // 폐쇄 이유
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
