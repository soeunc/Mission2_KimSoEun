package com.example.shoppingmall;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.jwt.JwtRequestDto;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        service.register(CustomUserDetails.builder()
                .username(username)
                .password(password)
                .build());
        return String.format("%s 회원 가입 완료!", username);
    }

    @PostMapping("login")
    public JwtResponseDto login(
            @RequestBody JwtRequestDto user
    ) {
        return service.login(user);
    }

    @PostMapping("/update-user")
    public String updateUser(
            @RequestParam("username") String username,
            @RequestBody CustomUserDetails user
    ) {
        service.updateUser(username, user);
        return String.format("%s, ROLE_USER로 전환!", username);
    }

    @PostMapping("/update-business")
    public String updateBusiness(
            @RequestParam("username") String username,
            @RequestBody CustomUserDetails user
    ) {
        service.updateBusinessUser(username, user);
        return String.format("%s, ROLE_BUSINESS_USER로 전환!", username);
    }
    @PutMapping("/{userId}/avatar")
    public UserDto avatar(
            @PathVariable("userId") Long userId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        return service.updateUserAvatar(userId, imageFile);
    }

}

