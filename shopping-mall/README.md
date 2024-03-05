# 테스트
테스트는 Postman에서 진행합니다.</br></br>
사용자는 비활성, 일반, 사업자, 관리자 사용자 4종류에서 일반 사용자인 판매자와 제안자까지 추가하여 진행하였습니다.</br></br>
발급 받은 토큰을 이용하여 인증을 진행합니다.</br></br>
관리자는 `id`: admin, `pw`: password로 로그인 후 토큰을 발급받습니다.

## `Auth`
### 회원 가입
POST / http://localhost:8080/users/register
- Postman Params에 `username`와 `password`를 입력 후 회원 가입 진행합니다. DB에 회원가입한 사용자가 들어왔는지 확인합니다.

### 사용자 프로필 업로드
PUT / http://localhost:8080/users/{userId}/avatar
- Body의 form-data `image` 입력 후 File 선택 후 프로필 업로드 합니다.
  src 폴더 위에 media 폴더가 생성되고, `userId` 폴더 안에 업로드 됐는지 확인합니다.

### 로그인
POST / http://localhost:8080/users/login 
- Body에 `username`와 `password`를 입력해 로그인합니다. 
- 토큰을 발급 받습니다. 토큰의 유효시간은 2주입니다.

### 일반 사용자로 전환
POST / http://localhost:8080/users/update-user 
- 비활성 사용자만 이용할 수 있습니다.
- Params에 `username`와 Body에 `nickname`, `name`, `age`, `email`, `phone`을 입력합니다.
- 로그인 시 반환 받은 토큰을 입력합니다.
  DB에 일반 사용자로 전환됐는지 확인합니다.

### 사업자 사용자로 전환
POST / http://localhost:8080/users/update-business 
- 일반 사용자만 이용할 수 있습니다.
- Params에 `username`와 Body에 `businessNumber`를 입력합니다.
- 로그인 시 반환 받은 토큰을 입력합니다.
  DB에 사업자 사용자로 전환과 쇼핑몰이 이 추가되었는지 확인합니다.

### 사업자 사용자 전환 목록 조회
GET / http://localhost:8080/users/read-business

### 관리자 사업자 사용자 전환 신청 응답
PUT / http://localhost:8080/users/{id}/businessStatus
- 사업자 사용자 전환 수락 또는 거절 할 수 있습니다. 
- 거절 시 일반 사용자로 유지됩니다.

## `Item`
### 중고거래 물품 등록
POST / http://localhost:8080/items/create
- 일반 사용자는 물품을 등록할 수 있다.
- 여러 명의 일반 사용자를 만듭니다.
- Body에 'title', 'description', 'minPrice'을 입력후 등록합니다. (대표 이미지는 반드시 등록할 필요없어서 제외하였습니다.)
- 판매자로 변하여 물품의 대한 수정과 삭제의 권한을 같습니다.

### 물품 전체 조회
GET / http://localhost:8080/items

### 물품 상세 조회
GET / http://localhost:8080/items/{itemId}

### 물품 수정
PUT /  http://localhost:8080/items/{itemId}/update
- 작성자(판매자)만 등록한 물픔을 수정할 수 있습니다.
- Body에 'title', 'description', 'minPrice'을 입력후 수정합니다.

### 물품 삭제
DELETE / http://localhost:8080/items/{itemId}/delete
- 작성자만 등록한 물품을 삭제할 수 있습니다.

### 물품 구매 제안
POST / http://localhost:8080/items/{itemId}/offers
- 토큰 입력 후 원하는 아이템에 구매 제안합니다. 

### 구매 제안 목록 조회(판매자)
GET / http://localhost:8080/items/offer/read-seller
- 판매자와 구매 제안한 사용자가 있어야합니다.

### 구매 제안 목록 조회(구매 제안한 사용자)
GET / http://localhost:8080/items/offer/read-offer
- 구매 제안한 사용자가 있어야 합니다.

### 판매자 구매 제안 응답
PUT / http://localhost:8080/items/{itemId}/response/{offerId}
- 아이템의 구매 제안 목록이 있어야 합니다.
- 아이템에 대하여 구매 제안한 사용자 중에서 수락, 거절할 수 있다.
- Params에 `response`입력 후 수락 또는 거절을 입력하여 응답합니다.

### 구매 제안 사용자 응답
PUT / http://localhost:8080/items/{itemId}/status/{offerId}
- 먼저 구매 제안한 사용자의 제안 목록을 조회하여 제안 상태가 수락된 물품을 확인합니다.
- 구매 제안 상태가 수락이면, 확정으로 변경됩니다.
- 확정된 후 다시 [판매자 구매 제안 응답](http://localhost:8080/shops/{itemId}/response/{offerId})을 조회하면
물품의 상태는 SOLD_OUT이 됩니다.
- **구현못한 부분**: 구매 제안을 수락후 확정되지 않은 다른 구매 제안의 상태는 모두 거절하는 구현이 안됨.

## `Shop`
### 쇼핑몰 개설 신청
POST / http://localhost:8080/shops/request-open
- **구현못한 부분**: 개설 신청에 대해서 요청을 보내야하는데 새로운 쇼핑몰이 개설이 되는 문제가 발생

### 쇼핑몰 정보 수정
PUT / http://localhost:8080/shops/{shopId}/update
- 분류 정보는 `ShopStatus`에 정해 놓은 분류로 지정해 줄 수 있습니다.

### 개설 신청한 쇼핑몰 조회
GET / http://localhost:8080/shops/list

### 개설 허가, 불가
POST / http://localhost:8080/shops/{shopId}/refusal

### 쇼핑몰 폐쇄 요청
POST / http://localhost:8080/shops/{shopId}/request-delete
- **구현못한 부분**: 쇼핑몰 개설 신청 요청과 같이 폐쇄 요청이 아닌 새로운 쇼핑몰이 개설되는 문제 발생

### 쇼핑몰 폐쇄
DELETE / http://localhost:8080/shops/{shopId}/delete

### 상품 등록
POST / http://localhost:8080/shops/{shopId}/create
- **구현못한 부분**: 상품 이미지 등록

### 상품 수정
PUT / http://localhost:8080/shops/{shopId}/{goodsId}/update

### 상품 삭제
DELETE / http://localhost:8080/shops/{shopId}/{goodsId}/delete

### 쇼핑몰 조회
GET / http://localhost:8080/shops/search
- **구현못한 부분**: 조건 없을 경우 최근 거래가 있던 순으로 조회가 문제
