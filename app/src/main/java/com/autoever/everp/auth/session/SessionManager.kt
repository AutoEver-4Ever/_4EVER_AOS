package com.autoever.everp.auth.session

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenStore: TokenStore,
) {
    private val _state = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val state: StateFlow<AuthState> = _state

    init {
        refreshFromStore()
    }

    fun refreshFromStore() {
        try {
            val token = tokenStore.getAccessToken()
            if (token.isNullOrEmpty()) {
                _state.value = AuthState.Unauthenticated
                Timber.tag(TAG).i("[INFO] 저장소에서 토큰이 없어 비인증 상태로 설정했습니다.")
            } else {
                _state.value = AuthState.Authenticated(token)
                Timber.tag(TAG).i("[INFO] 저장소의 토큰으로 인증 상태를 설정했습니다. (길이: ${token.length})")
            }
        } catch (e: Exception) {
            _state.value = AuthState.Unauthenticated
            Log.e(TAG, "[ERROR] 저장소에서 토큰을 불러오는 중 오류가 발생했습니다: ${e.message}")
        }
    }

    fun setAuthenticated(accessToken: String) {
        try {
            tokenStore.saveAccessToken(accessToken)
            _state.value = AuthState.Authenticated(accessToken)
            Timber.tag(TAG).i("[INFO] 인증 완료 상태로 전환했습니다. (토큰 길이: ${accessToken.length})")
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 인증 상태 설정 중 오류가 발생했습니다: ${e.message}")
        }
    }

    fun signOut() {
        try {
            tokenStore.clear()
            _state.value = AuthState.Unauthenticated
            Timber.tag(TAG).i("[INFO] 로그아웃 완료: 인증 상태를 해제했습니다.")
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 로그아웃 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private companion object {
        const val TAG = "SessionManager"
    }
}
