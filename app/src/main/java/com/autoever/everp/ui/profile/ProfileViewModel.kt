package com.autoever.everp.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.model.UserInfo
import com.autoever.everp.auth.repository.AuthRepository
import com.autoever.everp.auth.repository.UserRepository
import com.autoever.everp.auth.session.AuthState
import com.autoever.everp.auth.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: UserInfo? = null,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _ui = MutableStateFlow(ProfileUiState(isLoading = true))
    val ui: StateFlow<ProfileUiState> = _ui

    init {
        refresh()
    }

    fun refresh() {
        val st = sessionManager.state.value
        if (st !is AuthState.Authenticated) {
            _ui.value = ProfileUiState(isLoading = false, errorMessage = "인증이 필요합니다")
            return
        }
        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
                val info = userRepository.fetchUserInfo(st.accessToken)
                _ui.value = ProfileUiState(isLoading = false, user = info)
            } catch (e: Exception) {
                Log.e(TAG, "[ERROR] 프로필 조회 실패: ${e.message}")
                _ui.value = ProfileUiState(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun logout() {
        val st = sessionManager.state.value
        viewModelScope.launch {
            try {
                if (st is AuthState.Authenticated) {
                    // best-effort logout; ignore result
                    runCatching { authRepository.logout(st.accessToken) }
                }
            } finally {
                sessionManager.signOut()
            }
        }
    }

    private companion object { const val TAG = "ProfileVM" }
}

