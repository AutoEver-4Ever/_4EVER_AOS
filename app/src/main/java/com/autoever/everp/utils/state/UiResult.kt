package com.autoever.everp.utils.state

sealed interface UiResult<out T> {
    data class Success<out T>(
        val data: T,
    ) : UiResult<T>

    data class Error(
        val exception: Exception,
    ) : UiResult<Nothing>

    data object Loading : UiResult<Nothing>
}
