## 🥒 오이를 더 쉽게! OEasy WebSite 🥒

<br> 

- [🎀 ~~사이트로 이동해보세요!~~](https://oeasy.world/)
- 카카오톡으로 로그인 가능합니다.
- 저희와 함께 빠르게 오이와 가까워져 보세요, 💚
<br>

## 프로젝트 소개 🎇

- 여러분들은 **오이**에 대해 어떻게 생각하시나요.
- 호 불호가 극명한 우리 불쌍한 **오이**.. 얘기만 꺼내도 싫어하시나요?
- 보다 Easy하게 여러 사람들과 의견을 나누고 오이에 관련된 정보를 알아보아요.
<br>

### 주요 기능 ⚒

1. **아름다운 오이 메인화면** - 벌써 신나네요.  

2. **오이 꿀팁** - 오이의 비밀을 하나하나 보여드립니다.  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/13226453-791b-419c-9621-c736c50e0790" alt="오이 꿀팁">
   </details>

3. **오늘의 OE 지수** - 날씨🌈를 오이에게 접목시킨다면? 오이지수로 오늘 오이가 잘 자랄 수 있을지 알아봅시다.  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/5b7f71ab-a4e1-435f-ab80-60da3ae358f0" alt="OE 지수">
   </details>

4. **AI 오이** - AI 오이 챗봇과 함께 오이에 대해 대화를 나눠보아요!  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/65b19d07-87ce-4d48-8d99-23f40dd2c3b3" alt="AI 오이">
   </details>

5. **오이 가격 그래프** - 오늘 오이의 가격💰은 평균이 얼마인지, 지역별로는 얼마인지 궁금하셨죠? 저희가 제공해드려요.  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/6c85e99e-856f-4a59-a023-c611b7e498f2" alt="오이 가격 그래프">
   </details>

6. **다양하고 맛있는 오이 레시피** - 오이를 단순히 고명으로만 쓰셨다면, 이번에 레시피를 참고해주세요.  
   
7. **오이 커뮤니티** - 오이에 관해 편하게 의견을 나눌 수 있는 만남의 광장을 준비했습니다.  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/32d8880b-ab60-498a-84e4-50e018cebf27" alt="오이 커뮤니티">
   </details>

8. **오이 투표 & 실시간 채팅** - 개인의 취향에 맞추어 투표하세요. 이와 함께 실시간 채팅⌨으로 의견을 나눠봅시다.  
   <details>
   <summary> 🔽 </summary>
   <img src="https://github.com/user-attachments/assets/cff10803-35ee-442f-82fb-56f9ebb5f1f9" alt="오이 투표 & 실시간 채팅">
   </details>

9. **오이 정보** - 오이가 어떻게 저희를 만나게 되는지 정보를 알아보아요.

</div>

## 프로젝트 구조'

### 📌 프로젝트 아키텍처

![image](https://github.com/user-attachments/assets/7646c93b-1bd5-46d6-8003-d40b24af4d05)

### ✔ ERD

<details>
<summary> ERD 🔽 </summary>

![OEasy - ERD](https://github.com/user-attachments/assets/ed49980e-138f-46ee-a10f-6b37e39f752d)

</details>

---

# 📌 OEasy 프로젝트 폴더 구조

<details>
<summary> 🔽 </summary>

## 📂 `src/main/java/com/OEzoa/OEasy`

### 📌 `api` (컨트롤러 계층)
🚀 **역할:**  
- HTTP 요청을 받아 해당 기능의 서비스 로직을 실행  
- REST API 엔드포인트 제공  

🔹 **주요 기능:**  
- 사용자 인증 (`member/login`)  
- 게시판 관리 (`community`, `tip`)  
- 채팅 시스템 (`chatting`)  
- 투표 기능 (`vote`)  
- 레시피 관리 (`recipe`)  

---

### 📌 `application` (서비스 계층)
💡 **역할:**  
- 도메인 로직을 조합하여 애플리케이션의 주요 기능 수행  
- DTO 변환, 매퍼 및 검증 로직 포함  

🔹 **주요 기능:**  
- 회원 가입 및 로그인 처리 (`member`)  
- 데이터 가공 및 검증 (`validator`, `mapper`)  
- 비즈니스 로직 수행  

---

### 📌 `domain` (도메인 계층)
📌 **역할:**  
- 핵심 비즈니스 로직을 포함하는 엔티티 및 도메인 모델 관리  
- JPA 엔티티 및 관련 연산 정의  

🔹 **주요 기능:**  
- 회원 데이터 관리 (`member`)  
- 게시글 및 댓글 (`community`, `tip`)  
- 투표 시스템 (`vote`)  
- 이미지 및 파일 데이터 (`image`)  
- 알림 시스템 (`notification`)  

---

### 📌 `exception` (예외 처리 계층)
⚠️ **역할:**  
- 전역 및 도메인별 예외 정의 및 관리  
- 일관된 예외 처리 정책 제공  

🔹 **주요 기능:**  
- 공통 예외 처리  
- 사용자 인증 및 데이터 검증 예외 관리  

---

### 📌 `infra` (인프라 계층)
🛠 **역할:**  
- 외부 API 연동 및 설정 관련 클래스 포함  
- 데이터베이스, 외부 서비스 통신, 설정 정보 관리  

🔹 **주요 기능:**  
- Kakao API 연동 (`api/aioe`)  
- AWS S3 파일 업로드  
- Redis 캐싱  
- SMTP 이메일 전송  

---

### 📌 `util` (유틸리티 계층)
🛠 **역할:**  
- 공통적으로 사용되는 기능 제공 (파일 업로드, 시간 관리 등)  

🔹 **주요 기능:**  
- S3 업로드 (`s3Bucket`)  
- 스케줄러 (`scheduler`)  
- 시간 변환 및 추적 (`timeTrace`)  
- 인증 보조 기능  

---

✅ **특징:**  
- 기능 단위 패키지 구성으로 유지보수성 및 확장성 향상  
- DTO, Controller, Service, Validator 역할 명확한 분리  
- AOP 기반 인증/인가 처리
-

</details>
 
<br>

## 팀원 구성

<div>

|   **이름**   | **포지션** | **구분** | **Github** |   **이름**   | **포지션** | **구분** | **Github** |   **이름**   | **포지션** | **구분** |       **Github** |
|--------------|------------|----------|------------|--------------|------------|----------|------------|--------------|------------|----------|------------------|
| <div align="center"><img src="https://avatars.githubusercontent.com/u/96505736?v=4" width="50" height="50"/><br><b>김현빈</b></div> | <div align="center"><b>BE</b></div> | <div align="center"><b>팀장</b></div> | <div align="center"><b>[링크](https://github.com/khv9786)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/75283640?v=4" width="50" height="50"/><br><b>박진수</b></div> | <div align="center"><b>BE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/qkrwlstn1)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/103312634?v=4" width="50" height="50"/><br><b>임현아</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/lyuna29)</b></div> |
| <div align="center"><img src="https://avatars.githubusercontent.com/u/155044540?v=4" width="50" height="50"/><br><b>박수미</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/sumii-7)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/159214124?v=4" width="50" height="50"/><br><b>서샛별</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/ssbmel)</b></div> | <div align="center"><img src="https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQ9WjHnoAdJiBL5BrDMUCvvj04Okjl9zBJ5Xi8nVbMX0VLvvS4m" width="50" height="50"/><br><b>구현경</b></div> | <div align="center"><b>디자이너</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[Null](#)</b></div> |

</div>
