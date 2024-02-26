package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.jwt.JwtRequestDto;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import com.example.shoppingmall.repo.ItemRepository;
import com.example.shoppingmall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // 토큰을 통해서 인증을 진행하는데 따로 userId를 받아야하는지 토큰을 받아서 진행할 수 있지 않나..?

    // 물품 등록
    public ItemDto create(Long userId, ItemDto dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            // 일반 사용자인 경우에만 물품 등록 가능 -- 작성한 사용자의 유저id나 이름이 나왔으면 좋겠다.
            if (user.getAuthorities().equals(Role.ROLE_USER.name())) {
                Item item = Item.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .minPrice(dto.getMinPrice())
                        .state(State.SELLING.name())
                        .user(user)
                        .build();
                return ItemDto.fromEntity(itemRepository.save(item));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 물품 전체 보기
    public List<ItemDto> readAll() {
        return itemRepository.findAll().stream()
                .map(ItemDto::fromEntity)
                .toList();
    }

    // 물품 하나 보기
    public ItemDto readOne(Long id) {
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // 물품 수정 - 작성자만 가능
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (item.getUser().getId().equals(userId)) {
            item.setTitle(dto.getTitle());
            item.setDescription(dto.getDescription());
            item.setTitleImage(dto.getTitleImage());
            item.setMinPrice(dto.getMinPrice());

            return ItemDto.fromEntity(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 수정할 수 있습니다.");
        }

    }

    // 물품 삭제 - 작성자만 가능
    public void delete(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (item.getUser().getId().equals(userId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자만 삭제할 수 있습니다.");
        }
    }
}