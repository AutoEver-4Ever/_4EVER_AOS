package com.autoever.everp.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.AuthConfig
import com.autoever.everp.auth.AuthService
import com.autoever.everp.auth.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ExchangeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class AuthExchangeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _ui = MutableStateFlow(ExchangeUiState())
    val ui: StateFlow<ExchangeUiState> = _ui

    fun exchange(
        code: String,
        verifier: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        config: AuthConfig = AuthConfig.default(),
    ) {
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
                val token = AuthService.exchangeCodeForToken(config, code, verifier)
                sessionManager.setAuthenticated(token.access_token)
                _ui.value = _ui.value.copy(isLoading = false)
                onSuccess()
            } catch (e: Exception) {
                val msg = e.message ?: "토큰 교환 실패"
                _ui.value = _ui.value.copy(isLoading = false, errorMessage = msg)
                Log.e(TAG, "[ERROR] 토큰 교환 실패: ${msg}")
                onError(msg)
            }
        }
    }

    companion object {
        private const val TAG = "AuthExchangeVM"
    }
}

