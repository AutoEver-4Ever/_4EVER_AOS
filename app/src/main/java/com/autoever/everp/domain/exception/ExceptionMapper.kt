package com.autoever.everp.domain.exception

import android.database.SQLException
import android.database.sqlite.SQLiteException
import androidx.datastore.core.CorruptionException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.FileNotFoundException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Exception을 EverpException으로 변환하는 Mapper
 *
 * ### DataSource별 분리 이유
 *
 * FileNotFoundException은 IOException을 상속받기 때문에
 * Remote와 Local에서 동일한 IOException을 다르게 처리해야 함
 *
 * - Remote: FileNotFoundException → NetworkException (잘못된 매핑!)
 * - Local: FileNotFoundException → FileSystemException (올바른 매핑)
 *
 * ### 사용 방법
 * ```kotlin
 * // RemoteDataSource에서
 * Result.failure(RemoteExceptionMapper.map(e))
 *
 * // LocalDataSource에서
 * Result.failure(LocalExceptionMapper.map(e))
 * ```
 */
object ExceptionMapper {

    /**
     * 범용 매핑 (Context 없이 사용)
     *
     * 우선순위:
     * 1. EverpException → 그대로 반환
     * 2. HTTP 관련 → Remote 매핑
     * 3. Database 관련 → Local 매핑
     * 4. 공통 Exception → 공통 매핑
     */
    fun mapToEverpException(throwable: Throwable): EverpException {
        return when (throwable) {
            // 이미 EverpException인 경우
            is EverpException -> throwable

            // HTTP 관련은 Remote로 처리
            is HttpException -> RemoteExceptionMapper.map(throwable)

            // Database 관련은 Local로 처리
            is SQLiteException,
            is SQLException,
            is CorruptionException -> LocalExceptionMapper.map(throwable)

            // 나머지는 공통 처리
            else -> CommonExceptionMapper.map(throwable)
        }
    }
}

/**
 * Remote DataSource용 Exception Mapper
 * (Retrofit, Firebase 등)
 */
object RemoteExceptionMapper {

    fun map(throwable: Throwable): EverpException {
        return when (throwable) {
            is EverpException -> throwable

            // Retrofit HttpException (우선순위 높음)
            is HttpException -> mapHttpException(throwable)

            // 네트워크 관련 Exception
            // 주의: 구체적인 것부터 먼저 체크 (상속 관계 고려)
            is UnknownHostException -> NetworkException(
                "인터넷 연결을 확인할 수 없습니다.",
                throwable,
            )
            is SocketTimeoutException -> TimeoutException(
                "요청 시간이 초과되었습니다.",
                throwable,
            )
            // IOException은 마지막 (상위 클래스)
            is IOException -> NetworkException(
                "네트워크 연결에 실패했습니다.",
                throwable,
            )

            // JSON 파싱 오류
            is SerializationException -> DataParsingException(
                "데이터 형식이 올바르지 않습니다.",
                throwable,
            )

            // 나머지는 공통 매핑
            else -> CommonExceptionMapper.map(throwable)
        }
    }

    private fun mapHttpException(exception: HttpException): EverpException {
        return when (exception.code()) {
            400 -> BadRequestException(
                exception.message() ?: "잘못된 요청입니다.",
                exception,
            )
            401 -> UnauthorizedException(
                "인증이 필요합니다.",
                exception,
            )
            403 -> ForbiddenException(
                "접근 권한이 없습니다.",
                exception,
            )
            404 -> NotFoundException(
                "요청한 리소스를 찾을 수 없습니다.",
                exception,
            )
            409 -> DuplicateException(
                "이미 존재하는 데이터입니다.",
                exception,
            )
            in 500..599 -> ServerException(
                "서버 오류가 발생했습니다. (${exception.code()})",
                exception,
            )
            else -> UnknownException(
                "HTTP 오류가 발생했습니다. (${exception.code()})",
                exception,
            )
        }
    }
}

/**
 * Local DataSource용 Exception Mapper
 * (Room, DataStore, SharedPreferences, File, Cache 등)
 */
object LocalExceptionMapper {

    fun map(throwable: Throwable): EverpException {
        return when (throwable) {
            is EverpException -> throwable

            // Room Database 오류
            is SQLiteException,
            is SQLException -> DatabaseException(
                "데이터베이스 처리 중 오류가 발생했습니다: ${throwable.message}",
                throwable,
            )

            // DataStore 오류
            is CorruptionException -> DataStoreException(
                "저장된 데이터가 손상되었습니다.",
                throwable,
            )

            // 파일 시스템 오류 (IOException보다 먼저 체크!)
            is FileNotFoundException -> FileSystemException(
                "파일을 찾을 수 없습니다.",
                throwable,
            )

            // IOException (FileNotFoundException 이후에 체크)
            // Local에서 IOException은 파일/캐시 처리 오류
            is IOException -> CacheException(
                "데이터 저장 중 오류가 발생했습니다.",
                throwable,
            )

            // 나머지는 공통 매핑
            else -> CommonExceptionMapper.map(throwable)
        }
    }
}

/**
 * 공통 Exception Mapper
 * (모든 DataSource에서 공통으로 발생할 수 있는 Exception)
 */
object CommonExceptionMapper {

    fun map(throwable: Throwable): EverpException {
        return when (throwable) {
            is EverpException -> throwable

            // Security 관련
            is SecurityException -> PermissionDeniedException(
                permission = "UNKNOWN",
                message = "권한이 거부되었습니다: ${throwable.message}",
                cause = throwable,
            )

            // Validation 관련
            is IllegalArgumentException -> ValidationException(
                throwable.message ?: "잘못된 인자입니다.",
                throwable,
            )
            is IllegalStateException -> ValidationException(
                throwable.message ?: "잘못된 상태입니다.",
                throwable,
            )

            // NullPointerException (코드 버그)
            is NullPointerException -> UnknownException(
                "내부 오류가 발생했습니다(NPE): ${throwable.message}",
                throwable,
            )

            // 그 외 모든 Exception
            else -> UnknownException(
                throwable.message ?: "알 수 없는 오류가 발생했습니다.",
                throwable,
            )
        }
    }
}

/**
 * Extension Functions
 */

/** 범용 매핑 */
fun <T> Result<T>.mapException(): Result<T> {
    return this.onFailure { throwable ->
        throw ExceptionMapper.mapToEverpException(throwable)
    }
}

/** Remote용 매핑 */
fun <T> Result<T>.mapRemoteException(): Result<T> {
    return this.onFailure { throwable ->
        throw RemoteExceptionMapper.map(throwable)
    }
}

/** Local용 매핑 */
fun <T> Result<T>.mapLocalException(): Result<T> {
    return this.onFailure { throwable ->
        throw LocalExceptionMapper.map(throwable)
    }
}

