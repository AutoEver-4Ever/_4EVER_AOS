package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.auth.session.SessionManager
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.user.UserInfo
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository,
    private val sdRepository: SdRepository,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _customerDetail = MutableStateFlow<CustomerDetail?>(null)
    val customerDetail: StateFlow<CustomerDetail?> = _customerDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 사용자 정보 로드
                userRepository.getUserInfo().onSuccess { userInfo ->
                    _userInfo.value = userInfo
                    // 고객사 정보 로드
                    userInfo.userId.let { customerId ->
                        sdRepository.getCustomerDetail(customerId).onSuccess { customerDetail ->
                            _customerDetail.value = customerDetail
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "사용자 정보 로드 실패")
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

