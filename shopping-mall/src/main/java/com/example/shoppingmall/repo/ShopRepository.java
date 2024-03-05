package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.Enum.ShopCategory;
import com.example.shoppingmall.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findAllByOrderByIdDesc();
    List<Shop> findByShopName(String shopName);

    List<Shop> findByCategory(ShopCategory category);

    List<Shop> findByShopNameAndCategory(String shopName, ShopCategory category);

}
