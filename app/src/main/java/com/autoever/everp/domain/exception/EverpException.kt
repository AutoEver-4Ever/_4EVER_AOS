package com.autoever.everp.domain.exception

/**
 * EvERP 애플리케이션의 기본 Exception 클래스
 *
 * @property message 개발자용 상세 메시지 (로그에 기록)
 * @property cause 원인이 되는 Exception (stackTrace 추적용)
 *
 * ### cause의 역할
 * 1. **원인 추적**: Exception이 발생한 근본 원인을 추적
 *    - 예: NetworkException의 cause는 UnknownHostException
 * 2. **StackTrace 유지**: 원본 Exception의 stackTrace 보존
 * 3. **디버깅**: 로그에서 전체 Exception 체인 확인 가능
 *
 * ### 예제
 * ```kotlin
 * try {
 *     api.getData() // throws UnknownHostException
 * } catch (e: UnknownHostException) {
 *     throw NetworkException("네트워크 오류", cause = e)
 *     // cause를 통해 원본 UnknownHostException 정보 유지
 * }
 * ```
 */
sealed class EverpException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause) {

    /**
     * 사용자에게 표시할 메시지
     */
    abstract fun getUserMessage(): String

    /**
     * 에러 코드 (로깅 및 추적용)
     */
    abstract fun getErrorCode(): String

    /**
     * 재시도 가능 여부
     */
    open fun isRetryable(): Boolean = false
}

// ========== 네트워크 관련 Exception ==========

/**
 * 네트워크 연결 오류 -> IOException
 */
class NetworkException(
    message: String = "네트워크 연결에 실패했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "인터넷 연결을 확인해주세요."
    override fun getErrorCode() = "ERR_NETWORK"
    override fun isRetryable() = true
}

/**
 * 서버 오류 (5xx) -> HttpException,
 */
class ServerException(
    message: String = "서버에서 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "일시적인 서버 오류입니다. 잠시 후 다시 시도해주세요."
    override fun getErrorCode() = "ERR_SERVER"
    override fun isRetryable() = true
}

/**
 * 타임아웃 오류
 */
class TimeoutException(
    message: String = "요청 시간이 초과되었습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "요청 시간이 초과되었습니다. 다시 시도해주세요."
    override fun getErrorCode() = "ERR_TIMEOUT"
    override fun isRetryable() = true
}

// ========== 인증 관련 Exception ==========

/**
 * 인증 오류 (401)
 */
class UnauthorizedException(
    message: String = "인증이 필요합니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "로그인이 필요합니다."
    override fun getErrorCode() = "ERR_UNAUTHORIZED"
    override fun isRetryable() = false
}

/**
 * 권한 없음 (403)
 */
class ForbiddenException(
    message: String = "권한이 없습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "접근 권한이 없습니다."
    override fun getErrorCode() = "ERR_FORBIDDEN"
    override fun isRetryable() = false
}

/**
 * 토큰 만료
 */
class TokenExpiredException(
    message: String = "토큰이 만료되었습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "세션이 만료되었습니다. 다시 로그인해주세요."
    override fun getErrorCode() = "ERR_TOKEN_EXPIRED"
    override fun isRetryable() = false
}

// ========== 데이터 관련 Exception ==========

/**
 * 리소스를 찾을 수 없음 (404)
 */
class NotFoundException(
    message: String = "요청한 데이터를 찾을 수 없습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "요청한 정보를 찾을 수 없습니다."
    override fun getErrorCode() = "ERR_NOT_FOUND"
    override fun isRetryable() = false
}

/**
 * 잘못된 요청 (400)
 */
class BadRequestException(
    message: String = "잘못된 요청입니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "요청 내용을 확인해주세요."
    override fun getErrorCode() = "ERR_BAD_REQUEST"
    override fun isRetryable() = false
}

/**
 * 데이터 파싱 오류
 */
class DataParsingException(
    message: String = "데이터 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "데이터를 불러올 수 없습니다."
    override fun getErrorCode() = "ERR_DATA_PARSING"
    override fun isRetryable() = false
}

// ========== 비즈니스 로직 Exception ==========

/**
 * 유효성 검증 실패
 */
class ValidationException(
    message: String,
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = message
    override fun getErrorCode() = "ERR_VALIDATION"
    override fun isRetryable() = false
}

/**
 * 중복 데이터
 */
class DuplicateException(
    message: String = "이미 존재하는 데이터입니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = message
    override fun getErrorCode() = "ERR_DUPLICATE"
    override fun isRetryable() = false
}

// ========== Local Storage 관련 Exception ==========

/**
 * 데이터베이스 오류 (Room)
 */
class DatabaseException(
    message: String = "데이터베이스 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "데이터 저장 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_DATABASE"
    override fun isRetryable() = false
}

/**
 * 로컬 캐시 오류 (In-Memory Cache)
 */
class CacheException(
    message: String = "캐시 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "임시 데이터 처리 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_CACHE"
    override fun isRetryable() = true
}

/**
 * SharedPreferences 오류
 */
class PreferenceException(
    message: String = "설정 저장 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "설정을 저장할 수 없습니다."
    override fun getErrorCode() = "ERR_PREFERENCE"
    override fun isRetryable() = true
}

/**
 * DataStore 오류
 */
class DataStoreException(
    message: String = "데이터 저장소 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "데이터 저장 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_DATASTORE"
    override fun isRetryable() = true
}

/**
 * 파일 시스템 오류
 */
class FileSystemException(
    message: String = "파일 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "파일을 처리할 수 없습니다."
    override fun getErrorCode() = "ERR_FILE_SYSTEM"
    override fun isRetryable() = false
}

/**
 * 저장 공간 부족
 */
class StorageFullException(
    message: String = "저장 공간이 부족합니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "저장 공간이 부족합니다. 공간을 확보한 후 다시 시도해주세요."
    override fun getErrorCode() = "ERR_STORAGE_FULL"
    override fun isRetryable() = false
}

// ========== 기기 관련 Exception ==========

/**
 * 권한 없음 (Android Permission)
 */
class PermissionDeniedException(
    val permission: String,
    message: String = "필요한 권한이 없습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "앱을 사용하려면 권한이 필요합니다."
    override fun getErrorCode() = "ERR_PERMISSION_DENIED"
    override fun isRetryable() = false
}

/**
 * 기기 정보 접근 오류
 */
class DeviceInfoException(
    message: String = "기기 정보를 가져올 수 없습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "기기 정보를 확인할 수 없습니다."
    override fun getErrorCode() = "ERR_DEVICE_INFO"
    override fun isRetryable() = true
}

// ========== Firebase 관련 Exception ==========

/**
 * FCM 토큰 관련 오류
 */
class FcmTokenException(
    message: String = "FCM 토큰 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "알림 설정 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_FCM_TOKEN"
    override fun isRetryable() = true
}

/**
 * Firebase Crashlytics 오류
 */
class CrashlyticsException(
    message: String = "Crashlytics 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "로그 기록 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_CRASHLYTICS"
    override fun isRetryable() = false
}

/**
 * Firebase Analytics 오류
 */
class AnalyticsException(
    message: String = "Analytics 처리 중 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "분석 데이터 기록 중 오류가 발생했습니다."
    override fun getErrorCode() = "ERR_ANALYTICS"
    override fun isRetryable() = false
}

// ========== 기타 Exception ==========

/**
 * 알 수 없는 오류
 */
class UnknownException(
    message: String = "알 수 없는 오류가 발생했습니다.",
    cause: Throwable? = null,
) : EverpException(message, cause) {
    override fun getUserMessage() = "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
    override fun getErrorCode() = "ERR_UNKNOWN"
    override fun isRetryable() = false
}

