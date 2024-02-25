package com.example.shoppingmall;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.dto.RoleUserDetails;
import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService service;

    @PostMapping("/register")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password
            ) {
        service.register(CustomUserDetails.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
        return String.format("%s 회원 가입 완료!", username);
    }

    @PostMapping("login")
    public JwtResponseDto login(
            @RequestBody  CustomUserDetails user
    ) {
        return service.login(user);
    }

    @GetMapping("/home")
    public String home(
            Authentication authentication
    ) {
        log.info(authentication.getName());
        return "redirect:/shops/login";
    }

    // get 으로 일반사용자를 위한 정보를 넣을 수 있는 폼이 필요하다.

    @PostMapping("/update-user")
    public String updateUser(
            @RequestParam("username") String username,
            @RequestBody RoleUserDetails user
    ) {
        service.updateUser(username, user);
        return String.format("%s, ROLE_USER로 전환!", username);
    }

    @PostMapping("/update-business")
    public String updateBusiness(
            @RequestParam("username") String username,
            @RequestBody RoleUserDetails user
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

