package com.example.shoppingmall.repo;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
