# Exception 처리 가이드

## 개요

EvERP 앱의 중앙 집중식 Exception 처리 시스템입니다. 모든 예외를 일관되게 처리하고 사용자에게 적절한 메시지를 표시합니다.

## 구조

```
domain/exception/
├── EverpException.kt          # 기본 Exception 클래스들
├── ExceptionMapper.kt         # 일반 Exception → EverpException 변환
├── ExceptionHandler.kt        # 중앙 Exception 처리기
└── README.md                  # 이 파일

ui/common/
├── ErrorHandler.kt            # UI Layer Exception 처리
├── ErrorMessageHandler.kt     # Composable에서 에러 메시지 표시
└── ExceptionUsageExample.kt   # 사용 예제
```

## EverpException 종류

### 1. 네트워크 관련 (Remote)

- `NetworkException`: 네트워크 연결 오류 (재시도 가능)
- `ServerException`: 서버 오류 5xx (재시도 가능)
- `TimeoutException`: 요청 타임아웃 (재시도 가능)

### 2. 인증 관련

- `UnauthorizedException`: 인증 필요 401
- `ForbiddenException`: 권한 없음 403
- `TokenExpiredException`: 토큰 만료

### 3. 데이터 관련

- `NotFoundException`: 리소스 없음 404
- `BadRequestException`: 잘못된 요청 400
- `DataParsingException`: 데이터 파싱 오류

### 4. 비즈니스 로직

- `ValidationException`: 유효성 검증 실패
- `DuplicateException`: 중복 데이터

### 5. Local Storage 관련

- `DatabaseException`: Room 데이터베이스 오류
- `CacheException`: 인메모리 캐시 오류 (재시도 가능)
- `PreferenceException`: SharedPreferences 오류 (재시도 가능)
- `DataStoreException`: DataStore 오류 (재시도 가능)
- `FileSystemException`: 파일 시스템 오류
- `StorageFullException`: 저장 공간 부족

### 6. 기기 관련

- `PermissionDeniedException`: Android 권한 거부
- `DeviceInfoException`: 기기 정보 접근 오류 (재시도 가능)

### 7. Firebase 관련

- `FcmTokenException`: FCM 토큰 처리 오류 (재시도 가능)
- `CrashlyticsException`: Crashlytics 오류
- `AnalyticsException`: Analytics 오류

### 8. 기타

- `UnknownException`: 알 수 없는 오류

## 사용 방법

### 1. RemoteDataSource에서 사용

```kotlin
class UserHttpRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi,
) : UserRemoteDataSource {

    override suspend fun getUser(): Result<UserDto> {
        return try {
            val response = api.getUser()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(
                    BadRequestException(response.message ?: "사용자 조회 실패")
                )
            }
        } catch (e: Exception) {
            // ✅ RemoteExceptionMapper 사용
            Result.failure(RemoteExceptionMapper.map(e))
        }
    }
}
```

### 2. LocalDataSource에서 사용

```kotlin
class UserLocalDataSourceImpl @Inject constructor(
    private val dao: UserDao,
    private val dataStore: DataStore<Preferences>,
) : UserLocalDataSource {

    // Room Database
    override suspend fun saveUser(user: User) {
        try {
            dao.insert(user)
        } catch (e: Exception) {
            // ✅ LocalExceptionMapper 사용
            throw LocalExceptionMapper.map(e)
        }
    }

    // File 처리
    override suspend fun saveFile(file: File) {
        try {
            file.writeText("data")
        } catch (e: Exception) {
            // ✅ LocalExceptionMapper 사용
            throw LocalExceptionMapper.map(e)
            // FileNotFoundException → FileSystemException (올바른 매핑)
        }
    }
}
```

### 3. Repository에서 사용

```kotlin
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {

    override suspend fun getUser(): Result<User> {
        // Remote에서 가져오기 (이미 RemoteMapper 적용됨)
        return remoteDataSource.getUser()
            .map { dto -> dto.toDomain() }
            .onSuccess { user ->
                // Local에 저장
                try {
                    localDataSource.saveUser(user)
                } catch (e: Exception) {
                    // Local 오류는 무시하고 계속 진행
                    Timber.w(e, "사용자 로컬 저장 실패")
                }
            }
    }
}
```

### 4. ViewModel에서 사용

```kotlin
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val repository: ExampleRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val data: String? = null,
        val errorState: UiErrorHandler.UiErrorState? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorState = null)

            repository.getData()
                .onSuccess { data ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        data = data,
                    )
                }
                .onFailure { throwable ->
                    // Exception을 UiErrorState로 변환
                    val errorState = throwable.toUiErrorState(
                        onUnauthorized = {
                            // 로그인 화면으로 이동
                        }
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorState = errorState,
                    )
                }
        }
    }
}
```

### 5. Composable에서 사용

```kotlin
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorMessageHandler = remember(snackbarHostState, scope) {
        ErrorMessageHandler(snackbarHostState, scope)
    }

    // 에러 상태 감지 및 표시
    LaunchedEffect(uiState.errorState) {
        uiState.errorState?.let { errorState ->
            errorMessageHandler.showError(
                errorState = errorState,
                onRetry = if (errorState.isRetryable) {
                    { viewModel.loadData() }
                } else null,
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        // UI 구현
    }
}
```

### 6. 직접 Exception 발생

```kotlin
fun validateInput(input: String) {
    if (input.isBlank()) {
        throw ValidationException("입력값이 비어있습니다.")
    }
    if (input.length < 3) {
        throw ValidationException("최소 3자 이상 입력해주세요.")
    }
}
```

## ExceptionMapper 분리 구조

### 왜 Mapper를 분리했나요?

**문제:**
`FileNotFoundException`은 `IOException`을 상속받습니다. 단일 Mapper에서는:

```kotlin
// ❌ 잘못된 처리
when (throwable) {
    is IOException -> NetworkException(...)  // 먼저 체크
    is FileNotFoundException -> FileSystemException(...)  // 절대 실행 안됨!
}
```

**해결:**
DataSource별로 Mapper를 분리:

- **RemoteExceptionMapper**: `IOException` → `NetworkException`
- **LocalExceptionMapper**: `FileNotFoundException` → `FileSystemException` (먼저 체크)
- **CommonExceptionMapper**: 공통 Exception 처리

### Mapper 선택 가이드

| DataSource 타입 | 사용할 Mapper | 예제 |
|----------------|--------------|------|
| Remote (API, Firebase) | `RemoteExceptionMapper` | Retrofit, Firebase |
| Local (DB, File, Cache) | `LocalExceptionMapper` | Room, DataStore, File |
| Context 불명확 | `ExceptionMapper` | ViewModel 등 |

## 자동 변환 규칙

`ExceptionMapper`는 다음과 같이 자동 변환합니다:

| 원본 Exception           | 변환된 EverpException   | 재시도 가능 |
| ------------------------ | ----------------------- | ----------- |
| `HttpException(401)`     | `UnauthorizedException` | ❌          |
| `HttpException(403)`     | `ForbiddenException`    | ❌          |
| `HttpException(404)`     | `NotFoundException`     | ❌          |
| `HttpException(5xx)`     | `ServerException`       | ✅          |
| `UnknownHostException`   | `NetworkException`      | ✅          |
| `SocketTimeoutException` | `TimeoutException`      | ✅          |
| `IOException`            | `NetworkException`      | ✅          |
| `SerializationException` | `DataParsingException`  | ❌          |
| 기타                     | `UnknownException`      | ❌          |

## 로그 레벨

`ExceptionHandler`는 Exception 타입에 따라 로그 레벨을 자동 설정합니다:

- **ERROR**: `ServerException`, `UnknownException`
- **WARN**: `NetworkException`, `TimeoutException`
- **INFO**: `UnauthorizedException`, `TokenExpiredException`
- **DEBUG**: `ValidationException`, `BadRequestException`

## Best Practices

1. **Repository Layer에서 Exception 변환**

   - API 호출 시 발생하는 Exception을 적절한 EverpException으로 변환
   - `Result` 타입 사용 권장

2. **ViewModel에서 UI State 업데이트**

   - `toUiErrorState()` Extension 사용
   - 인증/권한 오류 시 콜백 처리

3. **Composable에서 사용자에게 표시**

   - `ErrorMessageHandler` 사용
   - 재시도 가능한 오류는 재시도 버튼 표시

4. **로그 활용**
   - 모든 Exception은 자동으로 로그 기록됨
   - 에러 코드로 추적 가능

## 예제 코드

전체 예제는 `ui/common/ExceptionUsageExample.kt` 파일을 참조하세요.
