package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
