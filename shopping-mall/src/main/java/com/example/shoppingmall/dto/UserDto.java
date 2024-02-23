package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String nickname;
    private String name;
    private String age;
    private String email;
    private String phone;
    private String authorities;
    private String avatar;

    // 일반 사용자가 되기 위한 서비스를 가지고 있다면
//    public boolean inactiveToUser() {
//        return nickname != null && name != null
//                && age != null && email != null && phone != null;
//    }

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .authorities(entity.getAuthorities())
                .build();
    }

}
