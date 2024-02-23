package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.entity.Role;
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
                .authorities(Role.ROLE_ADMIN.name())
                .build()
        );
    }

    @Override
    // formLogin 등 Spring Security 내부에서
    // 인증을 처리할 때 사용하는 메서드
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
//        if (optionalUser.isEmpty())
//            throw new UsernameNotFoundException(username);
//        UserEntity entity = optionalUser.get();

        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found" + username));


        return CustomUserDetails.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
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

            /*Role defaultRole = Role.ROLE_INACTIVE;// 기본 역할 설정
        if (Objects.equals(user.getUsername(), "admin"))
            defaultRole =  Role.ROLE_ADMIN;
        // TODO 이제 일반 사용자로 전화하는 로직만 추가해야한다.
        CustomUserDetails userDetails1 = new CustomUserDetails();
        if (userDetails1.inactiveToUser()) {
            defaultRole = Role.ROLE_USER;
            // 비활성 사용자에서 일반 사용자로 전환하는 로직 추가
            updateUserStatus(user.getUsername(), UserStatus.ACTIVE);
        }*/

        UserEntity newUser = UserEntity.builder()
                .username(userDetails.getUsername())
                // 비밀번호를 인코딩하여 안전하게 저장- 하려고 했더니 안된다.
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
