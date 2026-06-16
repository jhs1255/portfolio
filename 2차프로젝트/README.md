# KDT 2차 프로젝트

## 주제: JAVA GUI를 활용한 영화관 키오스크 프로그램 제작

* 제작기간 : 24.08.23 ~ 24.09.20

* 제작인원 : 5명 <br>
    - 맡은 역할 : <br>
        \- mysql workbench를 사용하여 데이터 전처리 \- <br>
            1. 영화진흥위원회에 있는 영화 정보를 가져와서 데이터 베이스에 저장 <br>
            2. 요구사항에 맞는 테이블 생성 및 관계 연결 <br><br>
        \- mysql JDBC를 사용하여 데이터베이스와 UI 연결 \- <br>
            1. UI에서 요청한 작업을 서버에서 처리한 후 DB에서 정보를 주고 받도록 구현 <br><br>
        \- 서버 개발 \- <br>
            1. 멀티 스레드와 웹소켓 통신을 활용하여 다중 접속 서버 개발 <br><br>
        \- 서버와 UI 연결 \- <br>
            1. UI 개발 <br>
            2. 연결에 필요한 UI를 수정하고 연결 <br><br>

* 프로젝트 목표
 
    1. 기존 영화관 키오스크 시스템을 벤치마킹해 업계 특성상 외래어 사용이 많은 화면과 기능을 고령층 등의 디지털 약자도 사용하기 편하도록 재구성 <br>
    2. 영화관 키오스크 프로그램을 통해 영화관은 티켓 예매 과정의 무인 시스템으로 운영의 효율성을 높이고, 직원은 고객 서비스 등에 집중함으로써 고객 만족도 상승 <br>
    3. Java GUI 활용을 통해 사용자 친화적 인터페이스 설계 능력 향상 <br>
    4. 요구사항에 따른 DB 모델 설계 및 구현을 통해 JDBC를 활용한 데이터베이스 접근 방법 학습 <br>
    5. 프로젝트 진행 단계별 산출물의 형상 관리를 통한 팀 협업 능력 향상 및 개인별 기술 스택 점검 <br>

* 프로젝트 내용

    사용기술

    - 프로그래밍 도구 : Eclipse IDE, MySQL workbench <br>
    - 사용 언어 : Java, SQL, FXML
 
    시연영상
    https://youtu.be/k6vADrJcfII<br>

    결과물<br>
    | **메인 화면** | **관리자 로그인 화면** | **관리자 메뉴 화면** |
    | :------------: | :------------: | :------------: |
    |<img src = "./문서/최종/img/1main.png" height="350px" width="270px"> | <img src = "./문서/최종/img/2admin_login.png" height="350px" width="270px"> | <img src = "./문서/최종/img/3adminmenu.png" height="350px" width="270px"> |
    | **영화 등록 화면** | **상영 시간 등록 화면** | **상영 정보 관리 화면** |
    |<img src = "./문서/최종/img/4movieregister.png" height="350px" width="270px"> | <img src = "./문서/최종/img/5movieregister.png" height="350px" width="270px"> | <img src = "./문서/최종/img/6movietimeinfo.png" height="350px" width="270px"> |
    | **상영 정보 추가/수정 화면** | **상영 정보 확인 화면** | **매출 확인 화면** |
    |<img src = "./문서/최종/img/7timeinfo.png" height="350px" width="270px"> | <img src = "./문서/최종/img/8timeinfo.png" height="350px" width="270px"> | <img src = "./문서/최종/img/9pricecheck.png" height="350px" width="270px"> |
    | **관리자 로그아웃 화면** | **티켓 구매 영화 선택 화면** | **인원 선택 화면** |
    |<img src = "./문서/최종/img/10adminlogout.png" height="350px" width="270px"> | <img src = "./문서/최종/img/11choicemovie.png" height="350px" width="270px"> | <img src = "./문서/최종/img/12choicepeople.png" height="350px" width="270px"> |
    | **좌석 선택 화면** | **결제 화면** | **예약 확정 화면** |
    |<img src = "./문서/최종/img/13choiceseat.png" height="350px" width="270px"> | <img src = "./문서/최종/img/14purchase.png" height="350px" width="270px"> | <img src = "./문서/최종/img/15ticketinfo.png" height="350px" width="270px"> |
    | **포인트 적립 화면** | **적립 완료 화면** | **예매 내역 조회 화면** |
    |<img src = "./문서/최종/img/16ticketphone.png" height="350px" width="270px"> | <img src = "./문서/최종/img/17ticketpoint.png" height="350px" width="270px"> | <img src = "./문서/최종/img/18ticketres.png" height="350px" width="270px"> |
    | **예매 내역 확인 화면** | **쉬운 화면 날짜 선택** | **쉬운 화면 영화선택** |
    |<img src = "./문서/최종/img/19ticketinformation.png" height="350px" width="270px"> | <img src = "./문서/최종/img/20simplechoice.png" height="350px" width="270px"> | <img src = "./문서/최종/img/21simplechoice2.png" height="350px" width="270px"> |
    | **쉬운 화면 시간 선택** | **쉬운 화면 좌석 선택** |
    |<img src="./문서/최종/img/22simple3.png" height="350px" width="270px"> | <img src="./문서/최종/img/23simplechoice4.png" height="350px" width="270px">
    - 좌석 자동 선택인 경우 바로 결제 화면으로 이동
    - 좌석 직접 선택인 경우 좌석 선택 화면으로 이동
