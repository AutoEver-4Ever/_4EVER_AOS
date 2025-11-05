package com.autoever.everp.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable에서 에러 메시지를 표시하기 위한 Helper
 */
class ErrorMessageHandler(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) {

    /**
     * 에러 메시지 표시
     */
    fun showError(
        errorState: UiErrorHandler.UiErrorState,
        onRetry: (() -> Unit)? = null,
    ) {
        val actionLabel = if (errorState.isRetryable && onRetry != null) "재시도" else null
        
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = errorState.message,
                actionLabel = actionLabel,
                duration = if (actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short,
            )
            
            if (result == SnackbarResult.ActionPerformed && onRetry != null) {
                onRetry()
            }
        }
    }

    /**
     * Throwable로부터 직접 에러 메시지 표시
     */
    fun showError(
        throwable: Throwable,
        onRetry: (() -> Unit)? = null,
        onUnauthorized: (() -> Unit)? = null,
    ) {
        val errorState = throwable.toUiErrorState(onUnauthorized = onUnauthorized)
        showError(errorState, onRetry)
    }

    /**
     * 일반 메시지 표시
     */
    fun showMessage(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
        }
    }
}

