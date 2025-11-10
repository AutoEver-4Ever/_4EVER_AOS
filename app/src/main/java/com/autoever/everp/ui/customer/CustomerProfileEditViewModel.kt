package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CustomerProfileEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        // Flow에서 profile 업데이트 구독
        profileRepository.observeProfile()
            .onEach { profile ->
                _profile.value = profile
            }
            .launchIn(viewModelScope)

        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            userRepository.getUserInfo().onSuccess { user ->
                _userInfo.value = user
                // 프로필 정보 로드
                profileRepository.refreshProfile(user.userType)
                    .onFailure { e ->
                        Timber.e(e, "프로필 정보 로드 실패")
                    }
            }.onFailure { e ->
                Timber.e(e, "사용자 정보 로드 실패")
            }
        }
    }

    fun saveProfile(
        companyName: String,
        businessNumber: String,
        baseAddress: String,
        detailAddress: String,
        officePhone: String,
        userPhoneNumber: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                // TODO: ProfileRepository에 updateProfile 메서드가 추가되면 구현
                // 현재는 Profile 정보만 표시하고, 업데이트 기능은 나중에 추가 예정
                Timber.w("프로필 업데이트 기능은 아직 구현되지 않았습니다.")
                // 임시로 성공 처리
                onSuccess()
            } catch (e: Exception) {
                Timber.e(e, "프로필 저장 실패")
            } finally {
                _isSaving.value = false
            }
        }
    }
}

