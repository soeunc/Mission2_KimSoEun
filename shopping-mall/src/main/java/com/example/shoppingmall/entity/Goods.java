package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private String image;
    @Setter
    private String description;
    @Setter
    private String price;
    @Setter
    private Integer stock;



    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

}
