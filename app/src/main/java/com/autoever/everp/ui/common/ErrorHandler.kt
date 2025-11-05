package com.autoever.everp.ui.common

import com.autoever.everp.domain.exception.ExceptionHandler
import com.autoever.everp.domain.exception.ForbiddenException
import com.autoever.everp.domain.exception.TokenExpiredException
import com.autoever.everp.domain.exception.UnauthorizedException
import timber.log.Timber

/**
 * UI Layer에서 사용하는 Error Handler
 */
object UiErrorHandler {

    /**
     * Exception을 처리하고 UI State를 업데이트하기 위한 정보 반환
     */
    fun handleError(
        throwable: Throwable,
        onUnauthorized: (() -> Unit)? = null,
        onForbidden: (() -> Unit)? = null,
    ): UiErrorState {
        val errorResult = ExceptionHandler.handle(throwable)

        // 특정 Exception에 대한 추가 처리
        when (errorResult.originalException) {
            is UnauthorizedException,
            is TokenExpiredException -> {
                Timber.tag("UiErrorHandler").i("인증 오류 발생 - 로그인 화면으로 이동 필요")
                onUnauthorized?.invoke()
            }
            is ForbiddenException -> {
                Timber.tag("UiErrorHandler").i("권한 없음 - 접근 제한")
                onForbidden?.invoke()
            }
            else -> {
                // 기타 오류는 별도 처리 없음
            }
        }

        return UiErrorState(
            message = errorResult.userMessage,
            errorCode = errorResult.errorCode,
            isRetryable = errorResult.isRetryable,
        )
    }

    /**
     * UI Error State
     */
    data class UiErrorState(
        val message: String,
        val errorCode: String,
        val isRetryable: Boolean,
    )
}

/**
 * ViewModel에서 사용할 수 있는 Extension Function
 */
fun Throwable.toUiErrorState(
    onUnauthorized: (() -> Unit)? = null,
    onForbidden: (() -> Unit)? = null,
): UiErrorHandler.UiErrorState {
    return UiErrorHandler.handleError(this, onUnauthorized, onForbidden)
}

