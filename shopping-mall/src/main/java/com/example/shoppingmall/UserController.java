package com.example.shoppingmall;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.dto.RoleUserDetails;
import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import com.example.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class UserController {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserService service;


    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        service.registerUser(CustomUserDetails.builder()
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

    // 일반 사용자로 전환
    // 비활성 사용자라고 인증된 사용자를 전환하기 위해서 인증 되었다는 것을 나타내야한다.
    @PostMapping("/update-status")
    public String updateUserStatus(
            @RequestParam("username") String username,
            @RequestBody RoleUserDetails user
    ) {
        service.updateUserStatus(username, user);
        return String.format("%s ROLE_USER으로 전환 성공!", username);
    }

//    @PostMapping("/changeUser")
//    public String changeUser(
//            @RequestParam("nickname") String nickname,
//            @RequestParam("name") String name,
//            @RequestParam("age") String age,
//            @RequestParam("email") String email,
//            @RequestParam("phone") String phone
//    ) {
//        service.updateUserStatus(RoleUserDetails.builder()
//                .nickname(nickname)
//                .name(name)
//                .age(age)
//                .email(email)
//                .phone(phone)
//                .authorities(Role.ROLE_USER.name())
//                .build());
//         // TODO 나중에 아이템 주문하는 곳으로 가도록 변경
//        // 권한 설정으로 비활성 사용자는 주문 페이지로 못가게 이동
//        return "redirect:/shops/login";
//    }
}

