package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Entity
@Builder
@Table(name = "item_table")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sellerName;
    @Column(nullable = false)
    @Setter
    private String title;
    @Column(nullable = false)
    @Setter
    private String description;
    @Setter
    private String titleImage;
    @Column(nullable = false)
    @Setter
    private Integer minPrice;
    @Setter
    private String status;
    @Setter
    private String response;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderOffer> orderOffers = new ArrayList<>();


}
