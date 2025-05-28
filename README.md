# 🔌 EV_charge: 전기차 충전소 정보 조회 웹사이트

> KEPCO OpenAPI를 활용하여 전국의 전기차 충전소 정보를 조회하고, 지도 기반 검색과 필터링을 지원하는 웹사이트 프로젝트입니다.

## 📌 프로젝트 개요

- **목표**: 사용자에게 실시간 전기차 충전소 정보를 제공하고, 지역 및 조건에 따른 검색 기능을 구현
- **데이터 출처**: KEPCO 전기차 충전소 OpenAPI (https://bigdata.kepco.co.kr)
- **기능 요약**:
  - 지역 기반(시/도, 시/군/구) 충전소 검색
  - 충전소 상세 정보 확인
  - 지도 API 연동 (예: Kakao Map, Naver Map 등)
  - 향후 예약 기능 및 혼잡도 예측 기능 확장 가능

---

## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Java, Spring Boot, MyBatis |
| Frontend | HTML, CSS, JavaScript, jQuery, JSP |
| Database | MySQL / MariaDB |
| API 연동 | KEPCO OpenAPI (REST) |
| 기타 | Maven, Tomcat, Git, Notion (문서 협업), Postman (테스트) |

---

## 🔍 주요 기능

### 1. 충전소 검색 기능
- 지역 선택 (시도코드 `metroCd`, 시군구코드 `cityCd`)
- 충전소 목록 출력
- 각 충전소 클릭 시 상세 정보 표시

### 2. 지도 연동 (선택)
- 충전소 위치 마커 표시
- 사용자의 현재 위치 기반 검색

---

## 📦 KEPCO OpenAPI 사용 방법

### 🔗 API 기본 정보
- **URL**: `https://bigdata.kepco.co.kr/openapi/v1/EVcharge.do`
- **요청 방식**: `GET`
- **응답 형식**: `XML` 또는 `JSON`
- **인증키 필요**: ✅

### 📥 예시 요청

```http
GET /openapi/v1/EVcharge.do?metroCd=11&cityCd=680&apiKey={YOUR_API_KEY}&returnType=JSON
