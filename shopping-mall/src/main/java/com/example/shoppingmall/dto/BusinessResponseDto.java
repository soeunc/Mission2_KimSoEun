package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponseDto {
    private Long businessId;
    private String name;
    private String authorities;

    public static BusinessResponseDto fromEntity(UserEntity entity) {
        return BusinessResponseDto.builder()
                .businessId(entity.getId())
                .name(entity.getName())
                .authorities(entity.getAuthorities())
                .build();
    }

}
