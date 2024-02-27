package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.OrderOfferDto;
import com.example.shoppingmall.entity.*;
import com.example.shoppingmall.repo.ItemRepository;
import com.example.shoppingmall.repo.OrderOfferRepository;
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
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderOfferRepository orderOfferRepository;

    // 물품 등록
    public ItemDto create(ItemDto dto) {
        // 현재 로그인한 사용자의 정보(권한) 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            // 권한 바꾸기
            user.setAuthorities(Role.ROLE_SELLER.name());
            userRepository.save(user);

            if (user.getAuthorities().equals(Role.ROLE_SELLER.name())) {
                Item item = Item.builder()
                        .sellerName(currentUsername)
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .minPrice(dto.getMinPrice())
                        .status(State.SELLING.name())
                        .user(user)
                        .build();

                return ItemDto.fromEntity(itemRepository.save(item));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
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
    public ItemDto update(Long itemId, ItemDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // User와 N:1관계 제거 필요...
        if (item.getUser().getAuthorities().equals(Role.ROLE_SELLER.name())) {
            if (item.getUser().getUsername().equals(currentUsername)) {
                item.setTitle(dto.getTitle());
                item.setDescription(dto.getDescription());
                item.setTitleImage(dto.getTitleImage());
                item.setMinPrice(dto.getMinPrice());

                return ItemDto.fromEntity(itemRepository.save(item));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 물품의 판매자가 아닙니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매자만 수정할 수 있습니다.");
        }
    }

    // 물품 삭제 - 작성자만 가능
    public void delete(Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (item.getUser().getAuthorities().equals(Role.ROLE_SELLER.name())) {
            if (item.getUser().getUsername().equals(currentUsername)) {
                itemRepository.deleteById(itemId);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 물품의 판매자가 아닙니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매자만 삭제할 수 있습니다.");
        }
    }

    // 구매 제안 등록
    public OrderOfferDto offer(Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            // 해당 아이템 정보 가져오기
            Optional<Item> optionalItem = itemRepository.findById(itemId);
            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();

                if (user.getAuthorities().equals(Role.ROLE_SELLER.name())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매자는 제안할 수 없습니다.");
                }

                user.setAuthorities(Role.ROLE_OFFER.name());
                userRepository.save(user);

                log.info("구매 제안자: {}", currentUsername);
                log.info("판매자: {}", item.getSellerName());

                OrderOffer offer = OrderOffer.builder()
                        .item(item)
                        .itemName(item.getTitle())
                        .offerName(currentUsername)
                        .user(item.getUser())
                        .build();
                // 아이템의 구매제안을 아이템에 추가
                item.getOrderOffers().add(offer);

                return OrderOfferDto.fromEntity(orderOfferRepository.save(offer));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "물품이 존재하지 않습니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");
        }
    }

    // 구매 제안 조회(판매자는 구매 제안 모두 조회)
    public List<OrderOfferDto> readSeller() {
        return orderOfferRepository.findAll().stream()
                .map(OrderOfferDto::fromEntity)
                .toList();
    }

    // 구매 제안 조회(제안 등록한 사용자는 자신의 제안 물품만 조회)
    public List<OrderOfferDto> readOffer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            List<OrderOffer> orderOffers = orderOfferRepository.findByOfferName(user.getUsername());

            if (!orderOffers.isEmpty()) {
                return orderOffers.stream()
                        .map(OrderOfferDto::fromEntity)
                        .toList();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "제안 목록이 없습니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 판매자의 구매 제안 응답
    public ItemDto updateResponse(Long itemId, Long offerId, String response) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OrderOffer offer = orderOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!item.getOrderOffers().isEmpty()) {
            if (offer.getOfferStatus().equals("수락") || offer.getOfferStatus().equals("거절")) {
                item.setResponse(response);
                return ItemDto.fromEntity(itemRepository.save(item));
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    // TODO 다시 생각해보기
    // 구매 제안 확정 사용자의 응답
    // 제안 응답(response)이 수락인 경우 구매 상태(offerStatus)가 확정으로 변경,
    // 확정으로 변경되면 물품의 상태(item.status)가 솔드아웃으로 변경
    // 또한 구매 상태가 확정이 아닌 경우에는 아이템의 구매제안(response)가 거절로 변경
    public ItemDto updateStatus(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (item.getResponse().equals("수락")) {
            OrderOffer offer = OrderOffer.builder()
                    .offerStatus("확정")
                    .build();

            orderOfferRepository.save(offer);
            item.setStatus(State.SOLD_OUT.name());
            return ItemDto.fromEntity(itemRepository.save(item));
        } else {
            item.setResponse("거절");
            return ItemDto.fromEntity(itemRepository.save(item));
        }
    }
}