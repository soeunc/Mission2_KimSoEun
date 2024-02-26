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
POST / http://localhost:8080/shops/{userId}/create
- 일반 사용자는 물품을 등록할 수 있다.
- 여러 명의 일반 사용자를 만듭니다.
- 토큰을 입력합니다.
- userId를 바꾸면서 물픔을 등록합니다.
- Body에 'title', 'description', 'minPrice'을 입력후 등록합니다. (대표 이미지는 반드시 등록할 필요없어서 제외하였습니다.)

### 물품 전체 조회
GET / http://localhost:8080/shops
- 토큰을 입력 후 조회합니다. 

### 물품 상세 조회
GET / http://localhost:8080/shops/{itemId}
- 토큰을 입력하고, 상세 조회할 Id로 조회합니다.

## 물픔 수정
PUT /  http://localhost:8080/shops/{userId}/{itemId}/update
- 작성자만 등록한 물픔을 수정할 수 있습니다.
- Body에 'title', 'description', 'minPrice'을 입력후 수정합니다.

## 물품 삭제
DELETE / http://localhost:8080/shops/{userId}/{itemId}/delete
- 작성자만 등록한 물품을 삭제할 수 있습니다.