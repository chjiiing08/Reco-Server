# Reco-Server

Reco 서비스의 Spring Boot 백엔드 서버입니다.

사용자 관리, 분석 기록 저장, 통계 조회, 지도 데이터 제공 및 소셜 로그인 기능을 담당합니다.

## 기술 스택

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Gradle
* Lombok

## 주요 기능

### 사용자 관리

* 회원가입
* 로그인
* 사용자 정보 조회

### 소셜 로그인

* Google 로그인
* Kakao 로그인

### 분석 기록 관리

* 분석 결과 저장
* 사용자별 분석 기록 조회
* 분석 결과 통계 제공

### 통계 기능

* 총 분석 횟수 조회
* 일별 분석 횟수 조회
* 월별 카테고리 통계 조회

### 지도 데이터 API

* 의류수거함 조회
* 폐건전지 수거함 조회
* 폐형광등 수거함 조회
* 재활용 정거장 조회

### 챗봇 메시지 관리

* 사용자별 채팅 내역 저장
* 채팅 기록 조회

## 프로젝트 구조

```text
src
 └─ main
     ├─ java
     │   └─ com.example.reco_test
     │       ├─ controller
     │       ├─ service
     │       ├─ repository
     │       ├─ entity
     │       ├─ dto
     │       └─ config
     │
     └─ resources
         ├─ application.yml
         └─ data
```

## 실행 방법

```bash
git clone <repository-url>
cd reco_test
```

```bash
./gradlew bootRun
```

## 데이터베이스

PostgreSQL 사용

`application.yml`에 DB 정보를 설정한 후 실행합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reco
    username: postgres
    password: password
```
