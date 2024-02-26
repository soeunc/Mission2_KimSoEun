package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String titleImage;
    @Setter
    private Integer minPrice;
    private String state;
    private UserEntity user;

    public static ItemDto fromEntity(Item entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .titleImage(entity.getTitleImage())
                .minPrice(entity.getMinPrice())
                .state(entity.getState())
                // 사용자 상세 정보 공개 x
//                .user(entity.getUser())
                .build();
    }
}
