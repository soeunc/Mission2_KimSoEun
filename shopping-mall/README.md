# 테스트 순서
테스트는 Postman에서 진행한다.

### 회원 가입
POST / http://localhost:8080/shops/register
- Postman Params에 `username`와 `password`를 입력 후 회원 가입 진행합니다. DB에 회원가입한 사용자가 들어왔는지 확인합니다.

### 사용자 프로필 업로드
PUT / http://localhost:8080/shops/{userId}/avatar
- Body의 form-data `image` 입력 후 File 선택 후 프로필 업로드 합니다.
  src 폴더 위에 media 폴더가 생성되고, `userId` 폴더 안에 업로드 됐는지 확인합니다.

### 로그인
POST / http://localhost:8080/shops/login 
- Body에 `username`와 `password`를 입력해 로그인합니다. 토큰을 반환 받습니다.

### 일반 사용자로 전환
POST / http://localhost:8080/shops/update-user 
- 비활성 사용자만 이용할 수 있습니다.
- Params에 `username`와 Body에 `nickname`, `name`, `age`, `email`, `phone`을 입력합니다.
- 로그인 시 반환 받은 토큰을 입력합니다.
  DB에 일반 사용자로 전환됐는지 확인합니다.

### 사업자 사용자로 전환
POST / http://localhost:8080/shops/update-business 
- 일반 사용자만 이용할 수 있습니다.
- Params에 `username`와 Body에 `businessNumber`를 입력합니다.
- 로그인 시 반환 받은 토큰을 입력합니다.
  DB에 사업자 사용자로 전환됐는지 확인합니다.
