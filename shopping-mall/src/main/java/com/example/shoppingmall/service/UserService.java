package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.dto.UserDto;
import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.jwt.JwtRequestDto;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import com.example.shoppingmall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public void register(UserDetails user) {
        if (manager.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다.");

        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            log.info("기존 유저 Encoded Password: {}", passwordEncoder.encode(userDetails.getPassword()));
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .authorities(Role.ROLE_INACTIVE.name())
                    .build();
            log.info("userDetails 비번 확인: {}", userDetails.getPassword());
            log.info("{} 회원 가입 완료", newUser);
            userRepository.save(newUser);
            log.info("user 비번 확인: {}", user.getPassword());
            log.info("newUser 비번 확인: {}", newUser.getPassword());
            log.info("userDetails 비번 확인: {}", userDetails.getPassword());

        } catch (ClassCastException e) {
            // 형변환 에러가 발생했을 때 예외처리(내부 서버 오류 처리)
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    public JwtResponseDto login(JwtRequestDto dto) {
        if (!manager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // 입력받은 아이디를 기준으로 아이디의 대한 정보르 usedetails에 할당
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        log.info("User 비밀번호: {}", dto.getPassword());
        log.info("userDetails 비밀번호: {}", userDetails.getPassword());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 토큰 발급
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        log.info("Token generated: {}", jwt);

        return response;
    }


    // 비활성 사용자 -> 일반 사용자 업데이트
    public void updateUser(String username, UserDetails user){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            CustomUserDetails customUserDetails = (CustomUserDetails) user;
            log.info("유저 확인: {}", existingUser);
            log.info("형변환 확인: {}", customUserDetails);

            if (customUserDetails.inactiveToUser()) {
                existingUser.setNickname(customUserDetails.getNickname());
                existingUser.setName(customUserDetails.getName());
                existingUser.setAge(customUserDetails.getAge());
                existingUser.setEmail(customUserDetails.getEmail());
                existingUser.setPhone(customUserDetails.getPhone());
                existingUser.setAuthorities(String.valueOf(Role.ROLE_USER));

                log.info("Switch {} to ROLE_USER", username);
                userRepository.save(existingUser);
            }
        } else {
            // 사용자가 존재하지 않는 경우의 처리
            log.info("{} is not found", username);
        }
    }

    // 일반 사용자 -> 사업자 사용자 업데이트
    // TODO 관리자가 전환신청 목록을 확인 및 수락, 거절 가능 (구현 못함)
    public void updateBusinessUser(String username, UserDetails user){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            CustomUserDetails customUserDetails = (CustomUserDetails) user;
            log.info("형변환 확인: {}", customUserDetails);

            if (customUserDetails.userToBusiness()) {
                existingUser.setBusinessNumber(customUserDetails.getBusinessNumber());
                existingUser.setAuthorities(String.valueOf(Role.ROLE_BUSINESS_USER));

                log.info("Switch {} to ROLE_BUSINESS_USER", username);
                userRepository.save(existingUser);
            }
        } else {
            // 사용자가 존재하지 않는 경우의 처리
            log.info("{} is not found", username);
        }
    }


    // 사용자 프로필 업로드
    public UserDto updateUserAvatar(Long id, MultipartFile image) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        String profileDir = String.format("media/%d/", id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.info("폴더 만들기 실패");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String originalFileName = image.getOriginalFilename();
        String[] fileNameSplit = originalFileName.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        log.info("확장명: {}", extension);
        String profileFileName = "profile." + extension;

        String profilePath = profileDir + profileFileName;
        log.info(profilePath);
        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.info("파일 저장 실패");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String requestPath = String.format("/static/%d/%s", id, profileFileName);
        log.info(requestPath);
        UserEntity userImage = optionalUser.get();
        userImage.setAvatar(requestPath);
        return UserDto.fromEntity(userRepository.save(userImage));
    }



}
