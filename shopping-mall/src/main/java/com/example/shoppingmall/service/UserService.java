package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.dto.RoleUserDetails;
import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.UserEntity;
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
import org.springframework.web.server.ResponseStatusException;

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
    public void registerUser(UserDetails user) {
        if (manager.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다.");

        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;

//            Role defaultRole = Role.ROLE_INACTIVE;// 기본 역할 설정

            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .authorities(Role.ROLE_INACTIVE.name())
                    .build();
            log.info("NewUser! {} 회원 가입 완료", newUser);
            userRepository.save(newUser);

        } catch (ClassCastException e) {
            // 형변환 에러가 발생했을 때 예외처리(내부 서버 오류 처리)
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    public JwtResponseDto login(CustomUserDetails user) {
        if (!manager.userExists(user.getUsername()))
            // 인증 되지 않았다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        UserDetails userDetails = manager.loadUserByUsername(user.getUsername());
        log.info("User 정보 확인: {}", userDetails.toString());
        log.info("User logged in: {}",userDetails.getUsername());

        if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");

        log.info("User1 logged in: {}", userDetails.getUsername());

        // 토큰 발급
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);

        log.info("Token generated: {}", jwt);

        return response;

    }


    // 사용자 권한 업데이트
    public String updateUserStatus(String username, UserDetails user){
        // 디비에 아이디가 존재하고, 추가 정보를 기입하지 않은 사용자는 업데이트 한다.

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            RoleUserDetails roleUserDetails = (RoleUserDetails) user;

            if (roleUserDetails.inactiveToUser()) {
                // 추가 정보가 있는 경우에만 업데이트 로직 수행
                existingUser.setNickname(roleUserDetails.getNickname());
                existingUser.setName(roleUserDetails.getName());
                existingUser.setAge(roleUserDetails.getAge());
                existingUser.setEmail(roleUserDetails.getEmail());
                existingUser.setPhone(roleUserDetails.getPhone());
                existingUser.setAuthorities(String.valueOf(Role.ROLE_USER));

                log.info("Switch {} to ROLE_USER", username);
                userRepository.save(existingUser);
            }

            return username;
        } else {
            // 사용자가 존재하지 않는 경우의 처리
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.");
        }




//        if (manager.userExists(username))
//            try {
//                RoleUserDetails roleUserDetails = (RoleUserDetails) user;
//
//                if (roleUserDetails.inactiveToUser())
//                    userRepository.updateUserStatus(username, Role.ROLE_USER.name());
//
//                UserEntity changeUser = UserEntity.builder()
//                        .username(roleUserDetails.getUsername())
//                        .password(passwordEncoder.encode(roleUserDetails.getPassword()))
//                        .nickname(roleUserDetails.getNickname())
//                        .name(roleUserDetails.getName())
//                        .age(roleUserDetails.getAge())
//                        .email(roleUserDetails.getEmail())
//                        .phone(roleUserDetails.getPhone())
//                        .authorities(String.valueOf(Role.ROLE_USER))
//                        .build();
//
//                // UserRepository 업데이트 로직 추가 (DB에 반영)
//                userRepository.save(changeUser);
//                log.info("Switch {} to ROLE_USER", username);
//
//            } catch (ClassCastException e) {
//                // 형변환 에러가 발생했을 때 예외처리(내부 서버 오류 처리)
//                log.error("Failed Cast to: {}", CustomUserDetails.class);
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//        return username;
//        if (userRepository.findByUsername(user.getUsername()).equals(user.getUsername()))
//        // 비활성 사용자에서 일반 사용자로 전환하는 로직 추가
//            if (user.getAuthorities().equals(Role.ROLE_INACTIVE))
//                if (user.inactiveToUser()) {
//                    userRepository.updateUserStatus(user.getUsername(), Role.ROLE_USER);
//                }
    }


    // 사용자 프로필 업로드
//    public UserDto updateUserAvatar(Long id, MultipartFile image) {
//        // 1. 유저의 존재 확인
//        Optional<UserEntity> optionalUser
//                = userRepository.findById(id);
//        if (optionalUser.isEmpty())
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//
//        // 2. 파일을 어디에 업로드 할건지 결정
//        // media/{id}/profile.{확장자}
//        // 2-1. (없다면) 폴더를 만들어야 한다. (media/{id}/)
//        String profileDir = String.format("media/%d/", id);
//        log.info(profileDir);
//        // 주어진 Path를 기준으로, 없는 모든 디렉토리를 생성하는 메서드
//        try {
//            Files.createDirectories(Path.of(profileDir));
//        } catch (IOException e) {
//            // 폴더를 만드는데 실패하면 기록을하고 사용자에게 알림
//            log.error(e.getMessage());
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }



}
