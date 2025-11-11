package com.autoever.everp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.model.UserInfo
import com.autoever.everp.auth.repository.UserRepository
import com.autoever.everp.auth.session.AuthState
import com.autoever.everp.auth.session.SessionManager
import com.autoever.everp.common.error.UnauthorizedException
import com.autoever.everp.domain.repository.AlarmRepository
import com.autoever.everp.domain.repository.DeviceInfoRepository
import com.autoever.everp.domain.repository.PushNotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val deviceInfoRepository: DeviceInfoRepository,
    private val pushNotificationRepository: PushNotificationRepository,
    private val alarmRepository: AlarmRepository,
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
                    Timber.tag(TAG).i(
                        "[INFO] 사용자 정보 로딩 완료 | " +
                            "userId=${info.userId ?: "null"}, " +
                            "userName=${info.userName ?: "null"}, " +
                            "email=${info.loginEmail ?: "null"}, " +
                            "role=${info.userRole ?: "null"}, " +
                            "userType=${info.userType ?: "null"}"
                    )
                    // 사용자 정보 로드 성공 후 FCM 토큰 등록
                    registerFcmToken()
                } catch (e: UnauthorizedException) {
                    Timber.tag(TAG).w("[WARN] 인증 만료로 로그아웃 처리")
                    sessionManager.signOut()
                } catch (e: Exception) {
                    Timber.tag(TAG).e("[ERROR] 사용자 정보 로드 실패: ${e.message}")
                }
            }
        } else {
            _user.value = null
        }
    }

    /**
     * FCM 토큰을 가져와서 서버에 등록합니다.
     */
    private suspend fun registerFcmToken() {
        try {
            // 1. FCM 토큰 가져오기
            val fcmToken = pushNotificationRepository.getToken()
            Timber.tag(TAG).d("[INFO] FCM 토큰 획득 성공")

            // 2. Android ID 가져오기
            val androidId = deviceInfoRepository.getAndroidId()
            Timber.tag(TAG).d("[INFO] Android ID 획득: $androidId")

            // 3. 서버에 FCM 토큰 등록
            alarmRepository.registerFcmToken(
                token = fcmToken,
                deviceId = androidId,
                deviceType = "ANDROID",
            )
            Timber.tag(TAG).i("[INFO] FCM 토큰 서버 등록 완료")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "[ERROR] FCM 토큰 등록 실패: ${e.message}")
            // FCM 토큰 등록 실패는 치명적이지 않으므로 로그만 남기고 계속 진행
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
