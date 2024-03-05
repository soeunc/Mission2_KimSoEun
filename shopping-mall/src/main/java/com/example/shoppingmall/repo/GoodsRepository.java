package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
