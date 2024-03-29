package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.entity.Enum.Role;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Slf4j
@Service
public class JpaUserDetailsService implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .name("Admin")
                .nickname("관리자_admin")
                .age("35")
                .email("admin@gmail.com")
                .phone("010-1234-1234")
                .authorities(Role.ROLE_ADMIN.name())
                .build()
        );
    }

    @Override
    // Spring Security 내부에서 인증을 처리할 때 사용하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        UserEntity entity = optionalUser.get();

        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .businessNumber(entity.getBusinessNumber())
                .authorities(entity.getAuthorities())
                .build();
    }


    @Override
    // 회원 가입 보안 확인
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다.");
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;

            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .authorities(userDetails.getRawAuthorities())
                    .build();

            userRepository.save(newUser);

        } catch (ClassCastException e) {
            // 형변환 에러가 발생했을 때 예외처리(내부 서버 오류 처리)
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }


}
