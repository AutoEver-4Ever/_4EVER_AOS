package com.autoever.everp.domain.exception

import timber.log.Timber

/**
 * 중앙 집중식 Exception 처리기
 */
object ExceptionHandler {

    /**
     * Exception을 처리하고 사용자 메시지를 반환
     */
    fun handle(throwable: Throwable): ErrorResult {
        val everpException = ExceptionMapper.mapToEverpException(throwable)

        // 로그 기록
        logException(everpException)

        return ErrorResult(
            userMessage = everpException.getUserMessage(),
            errorCode = everpException.getErrorCode(),
            isRetryable = everpException.isRetryable(),
            originalException = everpException,
        )
    }

    /**
     * Result에서 Exception을 처리
     */
    fun <T> handleResult(result: Result<T>): Result<T> {
        return result.onFailure { throwable ->
            handle(throwable)
        }
    }

    /**
     * Exception 로그 기록
     */
    private fun logException(exception: EverpException) {
        val tag = "ExceptionHandler"

        when (exception) {
            // 네트워크 오류는 경고 레벨
            is NetworkException,
            is TimeoutException -> {
                Timber.tag(tag).w(exception, "[${exception.getErrorCode()}] ${exception.message}")
            }

            // 인증 오류는 정보 레벨
            is UnauthorizedException,
            is TokenExpiredException -> {
                Timber.tag(tag).i(exception, "[${exception.getErrorCode()}] ${exception.message}")
            }

            // 검증 오류는 디버그 레벨
            is ValidationException,
            is BadRequestException -> {
                Timber.tag(tag).d(exception, "[${exception.getErrorCode()}] ${exception.message}")
            }

            // 서버 오류 및 알 수 없는 오류는 에러 레벨
            is ServerException,
            is UnknownException -> {
                Timber.tag(tag).e(exception, "[${exception.getErrorCode()}] ${exception.message}")
            }

            // 기타는 경고 레벨
            else -> {
                Timber.tag(tag).w(exception, "[${exception.getErrorCode()}] ${exception.message}")
            }
        }
    }

    /**
     * Exception 처리 결과
     */
    data class ErrorResult(
        val userMessage: String,
        val errorCode: String,
        val isRetryable: Boolean,
        val originalException: EverpException,
    )
}

/**
 * Extension function for Result
 */
fun <T> Result<T>.handleException(): Result<T> {
    return ExceptionHandler.handleResult(this)
}

/**
 * Extension function for Throwable
 */
fun Throwable.toErrorResult(): ExceptionHandler.ErrorResult {
    return ExceptionHandler.handle(this)
}

