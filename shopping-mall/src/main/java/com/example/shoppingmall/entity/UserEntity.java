package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
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
    @Setter
    private String businessStatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<OrderOffer> orderOffers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Shop> shops = new ArrayList<>();

}
