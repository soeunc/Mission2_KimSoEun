package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserEntity> findByAuthorities(String authorities);

}
