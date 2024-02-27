package com.example.shoppingmall.repo;

import com.example.shoppingmall.entity.OrderOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOfferRepository extends JpaRepository<OrderOffer, Long> {
    List<OrderOffer> findByOfferName(String name);
}

