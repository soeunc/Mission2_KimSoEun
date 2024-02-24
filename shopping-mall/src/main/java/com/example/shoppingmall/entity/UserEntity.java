package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
//@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Setter
    private String nickname;
    @Setter
    private String name;
    @Setter
    private String age;
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private String businessNumber;
    @Setter
    private String authorities;
    @Setter
    private String avatar;
}
