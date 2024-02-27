# 테스트 순서
테스트는 Postman에서 진행한다.

### 회원 가입
POST / http://localhost:8080/users/register
- Postman Params에 `username`와 `password`를 입력 후 회원 가입 진행합니다. DB에 회원가입한 사용자가 들어왔는지 확인합니다.

### 사용자 프로필 업로드
PUT / http://localhost:8080/users/{userId}/avatar
- Body의 form-data `image` 입력 후 File 선택 후 프로필 업로드 합니다.
  src 폴더 위에 media 폴더가 생성되고, `userId` 폴더 안에 업로드 됐는지 확인합니다.

### 로그인
POST / http://localhost:8080/users/login 
- Body에 `username`와 `password`를 입력해 로그인합니다. 토큰을 반환 받습니다.

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
  DB에 사업자 사용자로 전환됐는지 확인합니다.

### 중고거래 물품 등록
POST / http://localhost:8080/shops/create
- 일반 사용자는 물품을 등록할 수 있다.
- 여러 명의 일반 사용자를 만듭니다.
- 토큰을 입력합니다.
- Body에 'title', 'description', 'minPrice'을 입력후 등록합니다. (대표 이미지는 반드시 등록할 필요없어서 제외하였습니다.)
- 판매자로 변하여 물품의 대한 수정과 삭제의 권한을 같습니다.

### 물품 전체 조회
GET / http://localhost:8080/shops
- 토큰을 입력 후 조회합니다. 

### 물품 상세 조회
GET / http://localhost:8080/shops/{itemId}
- 토큰을 입력하고, 상세 조회할 Id로 조회합니다.

### 물품 수정
PUT /  http://localhost:8080/shops/{itemId}/update
- 작성자(판매자)만 등록한 물픔을 수정할 수 있습니다.
- 토큰 입력 후 Body에 'title', 'description', 'minPrice'을 입력후 수정합니다.

### 물품 삭제
DELETE / http://localhost:8080/shops/{itemId}/delete
- 작성자만 등록한 물품을 삭제할 수 있습니다.
- 토큰 입력 후 삭제합니다.

### 물품 구매 제안
POST / http://localhost:8080/shops/{itemId}/offers
- 토큰 입력 후 원하는 아이템에 구매 제안합니다. 
- 구매 제안한 사용자로 변합니다.

### 구매 제안 목록 조회(판매자)
GET / http://localhost:8080/shops/offer/read-seller
- 판매자와 구매 제안한 사용자가 있어야합니다.
- 토큰 입력 후 조회합니다.

### 구매 제안 목록 조회(구매 제안한 사용자)
GET / http://localhost:8080/shops/offer/read-offer
- 구매 제안한 사용자가 있어야 합니다.
- 토큰 입력 후 조회합니다.

### 판매자 구매 제안 응답
PUT / http://localhost:8080/shops/response/{itemId}/{offerId}
- 아이템에 대하여 구매 제안한 사용자 중에서 수락, 거절할 수 있다.
- 토큰 입력 후 Params에 `response`입력 후 수락 또는 거절을 입력하여 응답합니다.

### 구매 제안 사용자 응답
PUT / http://localhost:8080/shops/status/{itemId}
- 먼저 구매 제안한 사용자의 제안 목록을 조회하여 응답이 수락이 된 물품을 확인합니다.
- 구매 제안 응답이 수락이면, 등록한 제안의 상태가 확정으로 변경됩니다.
- 확정되면 물품은 SOLD_OUT 상태가 됩니다.


