package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
