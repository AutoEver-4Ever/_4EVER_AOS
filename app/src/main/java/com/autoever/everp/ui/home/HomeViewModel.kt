package com.autoever.everp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.AuthState
import com.autoever.everp.auth.SessionManager
import com.autoever.everp.network.GWService
import com.autoever.everp.network.UserInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    val authState: StateFlow<AuthState> = sessionManager.state

    private val _user = MutableStateFlow<UserInfoResponse?>(null)
    val user: StateFlow<UserInfoResponse?> = _user

    fun refreshUserIfAuthenticated() {
        val st = authState.value
        if (st is AuthState.Authenticated) {
            viewModelScope.launch {
                try {
                    val info = GWService.getUserInfo(st.accessToken)
                    _user.value = info
                    Log.i(TAG, "[INFO] 사용자 정보 로딩 완료")
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
