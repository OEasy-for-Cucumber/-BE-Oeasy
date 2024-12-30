# -BE--Oeasy


# 🥒 오이를 더 쉽게! OEasy WebSite 🥒

<br>

- [🎀 사이트로 이동해보세요!](https://oeasy.world/)
- 카카오톡으로 로그인 가능합니다.
- 저희와 함께 빠르게 오이와 가까워져 보세요, 💚
  
![image](https://github.com/user-attachments/assets/b883a868-f189-44bf-a6bb-be12a70cd101)

<br>

## 프로젝트 소개 🎇

- 여러분들은 **오이**에 대해 어떻게 생각하시나요.
- 호 불호가 극명한 우리 불쌍한 **오이**.. 얘기만 꺼내도 싫어하시나요?
- 보다 Easy하게 여러 사람들과 의견을 나누고 오이에 관련된 정보를 알아보아요.

<br>

- ### 주요 기능 ⚒
  - 1. **아름다운 오이 메인화면** - 벌써 신나네요.
  - 2. **오늘의 OE 지수** - 날씨🌈를 오이에게 접목시킨다면? 오이는 오늘 잘 자랄수 있을지 알아봅시다.
  - 3. **오이 꿀팁** - 오이의 비밀을 하나하나 보여드립니다.
  - 4. **오이 가격 그래프** - 오늘 오이의 가격💰은 평균이 얼마인지, 지역별로는 얼마인지 궁금하셨죠. 저희가 제공해드려요
  - 5. **다양하고 맛있는 오이 레시피** - 오이를 단순히 고명으로만 쓰셨다면, 이번에 레시피를 참고해주세요
  - 6. **오이 커뮤니티** - 오이에 관해 편하게 의견을 나눌 수 있는 만남의 광장을 준비했습니다.. 
  - 7. **오이 투표 & 실시간 채팅** - 개인의 취향에 맞추어 투표하세요. 이와 함께 실시간 채팅⌨으로 의견을 나눠봅시다. 

<br>

## 프로젝트 영상

</div>

// 영상 싹


</div>

## 프로젝트 구조

<details> <summary> ⛏ ERD </summary>

// 아키텍처


</details>

<details><summary>📂 파일 구조</summary>


    
```

├─build
│  ├─classes
│  │  └─java
│  │      └─main
│  │          └─com
│  │              └─als
│  │                  └─SMore
│  │                      ├─domain
│  │                      │  ├─entity
│  │                      │  └─repository
│  │                      ├─global
│  │                      │  └─json
│  │                      ├─log
│  │                      │  └─timeTrace
│  │                      ├─notification
│  │                      │  ├─controller
│  │                      │  ├─dto
│  │                      │  ├─repository
│  │                      │  └─service
│  │                      ├─study
│  │                      │  ├─attendance
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  │  ├─request
│  │                      │  │  │  └─response
│  │                      │  │  ├─service
│  │                      │  │  │  └─impl
│  │                      │  │  └─validator
│  │                      │  ├─calendar
│  │                      │  │  ├─controller
│  │                      │  │  ├─dto
│  │                      │  │  │  ├─request
│  │                      │  │  │  └─response
│  │                      │  │  ├─service
│  │                      │  │  │  └─impl
│  │                      │  │  └─validator
│  │                      │  ├─chatting
│  │                      │  │  ├─config
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  └─service
│  │                      │  ├─dashboard
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  ├─mapper
│  │                      │  │  └─service
│  │                      │  ├─enter
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  ├─mapper
│  │                      │  │  └─service
│  │                      │  ├─management
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  ├─mapper
│  │                      │  │  └─service
│  │                      │  ├─notice
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  ├─service
│  │                      │  │  └─validator
│  │                      │  ├─problem
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  │  ├─request
│  │                      │  │  │  │  ├─problem
│  │                      │  │  │  │  └─problemBank
│  │                      │  │  │  └─response
│  │                      │  │  │      ├─problem
│  │                      │  │  │      └─problemBank
│  │                      │  │  ├─service
│  │                      │  │  │  └─impl
│  │                      │  │  │      ├─problem
│  │                      │  │  │      └─problemBank
│  │                      │  │  └─validator
│  │                      │  ├─studyCRUD
│  │                      │  │  ├─controller
│  │                      │  │  ├─DTO
│  │                      │  │  ├─mapper
│  │                      │  │  ├─service
│  │                      │  │  └─utils
│  │                      │  └─todo
│  │                      │      ├─controller
│  │                      │      ├─DTO
│  │                      │      ├─mapper
│  │                      │      └─service
│  │                      └─user
│  │                          ├─login
│  │                          │  ├─config
│  │                          │  ├─controller
│  │                          │  ├─dto
│  │                          │  │  └─response
│  │                          │  ├─service
│  │                          │  └─util
│  │                          │      └─aop
│  │                          │          ├─annotation
│  │                          │          └─dto
│  │                          ├─mypage
│  │                          │  ├─config
│  │                          │  ├─controller
│  │                          │  ├─dto
│  │                          │  │  ├─request
│  │                          │  │  └─response
│  │                          │  └─service
│  │                          └─mystudy
│  │                              ├─controller
│  │                              ├─dto
│  │                              │  ├─request
│  │                              │  └─response
│  │                              └─service

 ```

</details>

<br>

<br>
## 프로젝트 아키텍처

<br>

## 팀원 구성

<div>

|   **이름**   | **포지션** | **구분** | **Github** |   **이름**   | **포지션** | **구분** | **Github** |   **이름**   | **포지션** | **구분** |       **Github** |
|--------------|------------|----------|------------|--------------|------------|----------|------------|--------------|------------|----------|------------------|
| <div align="center"><img src="https://avatars.githubusercontent.com/u/96505736?v=4" width="50" height="50"/><br><b>김현빈</b></div> | <div align="center"><b>BE</b></div> | <div align="center"><b>팀장</b></div> | <div align="center"><b>[링크](https://github.com/khv9786)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/75283640?v=4" width="50" height="50"/><br><b>박진수</b></div> | <div align="center"><b>BE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/qkrwlstn1)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/103312634?v=4" width="50" height="50"/><br><b>임현아</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/lyuna29)</b></div> |
| <div align="center"><img src="https://avatars.githubusercontent.com/u/155044540?v=4" width="50" height="50"/><br><b>박수미</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/sumii-7)</b></div> | <div align="center"><img src="https://avatars.githubusercontent.com/u/159214124?v=4" width="50" height="50"/><br><b>서샛별</b></div> | <div align="center"><b>FE</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[링크](https://github.com/ssbmel)</b></div> | <div align="center"><img src="https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQ9WjHnoAdJiBL5BrDMUCvvj04Okjl9zBJ5Xi8nVbMX0VLvvS4m" width="50" height="50"/><br><b>구현경</b></div> | <div align="center"><b>디자이너</b></div> | <div align="center"><b>팀원</b></div> | <div align="center"><b>[Null](#)</b></div> |

</div>
