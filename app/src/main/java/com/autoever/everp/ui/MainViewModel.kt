package com.autoever.everp.ui

import androidx.lifecycle.ViewModel
import com.autoever.everp.domain.model.user.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        // TODO: AuthRepository 주입 예정
    ) : ViewModel() {
        private val _userRole = MutableStateFlow<UserRole>(UserRole.VENDOR) // TODO 초기값 NONE으로 변경 예정
        val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

        /**
         * 사용자 역할 설정 (로그인 후 호출)
         */
        fun setUserRole(role: UserRole) {
            _userRole.value = role
        }

        /**
         * 사용자 역할 설정 (문자열로)
         */
        fun updateUserRole(roleString: String) {
            _userRole.value = UserRole.fromStringOrDefault(roleString)
        }

        /**
         * 사용자 역할 초기화 (로그아웃 시 호출)
         */
        fun clearUserRole() {
            _userRole.value = UserRole.NONE
        }

        /**
         * 현재 역할이 고객사인지 확인
         */
        fun isCustomer(): Boolean = _userRole.value == UserRole.CUSTOMER

        /**
         * 현재 역할이 공급사인지 확인
         */
        fun isVendor(): Boolean = _userRole.value == UserRole.VENDOR
    }
