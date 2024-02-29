package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ShopDto;
import com.example.shoppingmall.entity.Enum.ShopStatus;
import com.example.shoppingmall.entity.Shop;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repo.ShopRepository;
import com.example.shoppingmall.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    // 쇼핑몰 정보 수정
    public ShopDto updateShop(Long shopId, ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            Shop shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            // 로그인한 사용자가 쇼핑몰의 주인인지 확인하기
            if (!user.getShops().contains(shop)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            shop.setShopName(dto.getShopName());
            shop.setIntroduction(dto.getIntroduction());
            shop.setCategory(dto.getCategory());

            return ShopDto.fromEntity(shopRepository.save(shop));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // TODO 신청하는 건데 새로운 쇼핑몰이 개설된다.
    // 쇼핑몰 개설 신청
    public ShopDto requestOpen(ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            // 쇼핑몰의 정보가 모두 작성되어있는지 확인
            log.info("실행 확인");
            Shop shop = Shop.builder()
                    .shopName(dto.getShopName())
                    .introduction(dto.getIntroduction())
                    .category(dto.getCategory())
                    .build();

            user.getShops().add(shop);
            userRepository.save(user);

            return ShopDto.fromEntity(shopRepository.save(shop));

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 개설 신청 목록
    public List<ShopDto> shopList() {
        return shopRepository.findAll().stream()
                .map(ShopDto::fromEntity)
                .toList();
    }


    // 개설 허가, 불가
    public ShopDto refusal(Long shopId, ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {

            Optional<Shop> optionalShop = shopRepository.findById(shopId);
            if (optionalShop.isPresent()) {
                Shop shop = optionalShop.get();
                if (dto.getShopResponse().equals("허가")) {
                    shop.setShopResponse(dto.getShopResponse());
                    shop.setShopStatus(ShopStatus.OPEN.name());

                    return ShopDto.fromEntity(shopRepository.save(shop));
                } else if (dto.getShopResponse().equals("불가") && dto.getReason() != null) {
                    shop.setShopResponse(dto.getShopResponse());
                    shop.setReason(dto.getReason());

                    return ShopDto.fromEntity(shopRepository.save(shop));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // TODO 쇼핑몰 신청 요청과 같은 문제
    // 쇼핑몰 페쇄 요청
    public ShopDto requestDel(Long shopId, ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            Optional<Shop> optionalShop = shopRepository.findById(shopId);
            if (optionalShop.isPresent()) {
                if (dto.getDeleteReason() != null) {
                    Shop shop = Shop.builder()
                            .deleteReason(dto.getDeleteReason())
                            .build();

                    user.getShops().add(shop);
                    userRepository.save(user);

                    return ShopDto.fromEntity(shopRepository.save(shop));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 페쇄 수락
    public void deleteShop(Long shopId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            shopRepository.deleteById(shopId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
