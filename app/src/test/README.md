# 테스트 코드 가이드

이 디렉토리에는 프로젝트의 단위 테스트 코드가 포함되어 있습니다.

## 테스트 구조

### ViewModel 테스트

- `ui/MainViewModelTest.kt` - MainViewModel 테스트
- `ui/customer/CustomerHomeViewModelTest.kt` - CustomerHomeViewModel 테스트
- `ui/customer/NotificationViewModelTest.kt` - NotificationViewModel 테스트

### Repository 테스트

- `data/repository/AlarmRepositoryImplTest.kt` - AlarmRepositoryImpl 테스트

### Domain Model 테스트

- `domain/model/user/UserTypeEnumTest.kt` - UserTypeEnum 테스트
- `domain/model/notification/NotificationTest.kt` - Notification 및 NotificationCount 테스트

## 테스트 실행 방법

### 모든 테스트 실행

```bash
./gradlew test
```

### 특정 테스트 클래스 실행

```bash
./gradlew test --tests "com.autoever.everp.ui.MainViewModelTest"
```

### 특정 테스트 메서드 실행

```bash
./gradlew test --tests "com.autoever.everp.ui.MainViewModelTest.초기 상태는 UNKNOWN이어야 함"
```

### 테스트 리포트 확인

테스트 실행 후 리포트는 다음 위치에서 확인할 수 있습니다:

```
app/build/reports/tests/test/index.html
```

## 사용된 테스트 라이브러리

- **JUnit 4** - 기본 테스트 프레임워크
- **MockK** - Mocking 라이브러리
- **Turbine** - Flow 테스트를 위한 라이브러리
- **Kotlinx Coroutines Test** - 코루틴 테스트 지원

## 테스트 작성 가이드

### ViewModel 테스트 작성 시

1. MockK를 사용하여 Repository를 모킹합니다
2. `runTest`를 사용하여 코루틴 테스트를 작성합니다
3. `advanceUntilIdle()`을 사용하여 비동기 작업이 완료될 때까지 대기합니다
4. StateFlow의 값을 검증합니다

### Repository 테스트 작성 시

1. LocalDataSource와 RemoteDataSource를 모킹합니다
2. Flow 테스트는 Turbine을 사용합니다
3. `runTest`를 사용하여 suspend 함수를 테스트합니다

### Domain Model 테스트 작성 시

1. 순수 함수이므로 모킹 없이 직접 테스트합니다
2. 모든 enum 값과 경계 케이스를 테스트합니다

## 향후 추가 예정 테스트

- DataSource 테스트
- UI 테스트 (Compose Test)
- 통합 테스트
