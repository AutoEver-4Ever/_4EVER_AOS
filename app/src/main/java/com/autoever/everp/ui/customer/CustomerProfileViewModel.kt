package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.session.SessionManager
import com.autoever.everp.domain.model.profile.Profile
import com.autoever.everp.domain.model.user.UserInfo
import com.autoever.everp.domain.repository.ProfileRepository
import com.autoever.everp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Flow에서 profile 업데이트 구독
        profileRepository.observeProfile()
            .onEach { profile ->
                _profile.value = profile
            }
            .launchIn(viewModelScope)

        loadUserInfo()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 사용자 정보 로드
                userRepository.getUserInfo().onSuccess { userInfo ->
                    _userInfo.value = userInfo
                    // 프로필 정보 로드
                    profileRepository.refreshProfile(userInfo.userType)
                        .onFailure { e ->
                            Timber.e(e, "프로필 정보 로드 실패")
                        }
                }.onFailure { e ->
                    Timber.e(e, "사용자 정보 로드 실패")
                }
            } catch (e: Exception) {
                Timber.e(e, "정보 로드 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadUserInfo()
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionManager.signOut()
            try {
                userRepository.logout()
                onSuccess()
                Timber.i("로그아웃 성공")
            } catch (e: Exception) {
                Timber.e(e, "로그아웃 실패")
            }
        }
    }
}

