package com.example.shoppingmall.entity;

import com.example.shoppingmall.entity.Enum.ShopCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String shopName;
    @Setter
    private String introduction;
    @Setter
    private ShopCategory category;
    @Setter
    private String shopStatus;
    @Setter
    private String shopResponse;
    @Setter
    private String reason;
    @Setter
    private String deleteReason;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Goods> goods = new ArrayList<>();
}
