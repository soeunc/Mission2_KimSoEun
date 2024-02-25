package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Setter
    private String avatar;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .avatar(entity.getAvatar())
                .build();
    }

}
