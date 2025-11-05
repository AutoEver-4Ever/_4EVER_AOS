package com.autoever.everp.auth.session

import com.autoever.everp.common.annotation.ApplicationScope
import com.autoever.everp.data.datasource.local.AuthLocalDataSource
import com.autoever.everp.domain.model.auth.AccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    private val _state = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val state: StateFlow<AuthState> = _state

    init {
        applicationScope.launch {
            refreshFromStore()
        }
    }

    fun refreshFromStore() {
        applicationScope.launch {
            try {
                val token = authLocalDataSource.getAccessToken()
                if (token.isNullOrEmpty()) {
                    _state.value = AuthState.Unauthenticated
                    Timber.tag(TAG).i("[INFO] 저장소에서 토큰이 없어 비인증 상태로 설정했습니다.")
                } else {
                    _state.value = AuthState.Authenticated(token)
                    Timber.tag(TAG).i("[INFO] 저장소의 토큰으로 인증 상태를 설정했습니다. (길이: ${token.length})")
                }
            } catch (e: Exception) {
                _state.value = AuthState.Unauthenticated
                Timber.tag(TAG).e(e, "[ERROR] 저장소에서 토큰을 불러오는 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    fun setAuthenticated(accessToken: String) {
        applicationScope.launch {
            try {
                authLocalDataSource.saveAccessToken(
                    AccessToken(
                        token = accessToken,
                        type = "Bearer",
                        expiresIn = LocalDateTime.now().plusHours(1), // 기본값: 1시간 후
                    ),
                )
                _state.value = AuthState.Authenticated(accessToken)
                Timber.tag(TAG).i("[INFO] 인증 완료 상태로 전환했습니다. (토큰 길이: ${accessToken.length})")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "[ERROR] 인증 상태 설정 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    fun signOut() {
        applicationScope.launch {
            try {
                authLocalDataSource.clearAccessToken()
                _state.value = AuthState.Unauthenticated
                Timber.tag(TAG).i("[INFO] 로그아웃 완료: 인증 상태를 해제했습니다.")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "[ERROR] 로그아웃 처리 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    private companion object {
        const val TAG = "SessionManager"
    }
}
