package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.Role;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.entity.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    // 사용자의
    // 권한을 업데이트하기위한 쿼리
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.authorities = :newAuthorities WHERE u.username = :username")
    void updateUserStatus(
            @Param("username") String username,
            @Param("newAuthorities") String newAuthorities
    );
}
