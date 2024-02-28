package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    @Setter
    private String avatar;
    private String authorities;
    @Setter
    private String businessStatus;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .avatar(entity.getAvatar())
                .authorities(entity.getAuthorities())
                .businessStatus(entity.getBusinessStatus())
                .build();
    }

}
