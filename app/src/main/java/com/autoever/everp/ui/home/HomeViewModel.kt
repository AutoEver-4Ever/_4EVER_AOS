package com.autoever.everp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.session.AuthState
import com.autoever.everp.auth.session.SessionManager
import com.autoever.everp.auth.repository.UserRepository
import com.autoever.everp.auth.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.autoever.everp.common.error.UnauthorizedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
) : ViewModel() {
    val authState: StateFlow<AuthState> = sessionManager.state

    private val _user = MutableStateFlow<UserInfo?>(null)
    val user: StateFlow<UserInfo?> = _user

    fun refreshUserIfAuthenticated() {
        val st = authState.value
        if (st is AuthState.Authenticated) {
            viewModelScope.launch {
                try {
                    val info = userRepository.fetchUserInfo(st.accessToken)
                    _user.value = info
                    Log.i(
                        TAG,
                        "[INFO] 사용자 정보 로딩 완료 | " +
                            "userId=${info.userId ?: "null"}, " +
                            "userName=${info.userName ?: "null"}, " +
                            "email=${info.loginEmail ?: "null"}, " +
                            "role=${info.userRole ?: "null"}, " +
                            "userType=${info.userType ?: "null"}"
                    )
                } catch (e: UnauthorizedException) {
                    Log.w(TAG, "[WARN] 인증 만료로 로그아웃 처리")
                    sessionManager.signOut()
                } catch (e: Exception) {
                    Log.e(TAG, "[ERROR] 사용자 정보 로드 실패: ${e.message}")
                }
            }
        } else {
            _user.value = null
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
