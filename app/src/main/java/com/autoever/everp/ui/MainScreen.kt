package com.autoever.everp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.autoever.everp.domain.model.user.UserRole
import com.autoever.everp.ui.customer.CustomerApp
import com.autoever.everp.ui.login.LoginScreen
import com.autoever.everp.ui.vendor.VendorApp

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    // ViewModel로부터 사용자 역할 상태를 관찰
    val userRole by viewModel.userRole.collectAsState()

    // 역할 상태에 따라 적절한 UI를 렌더링
    when (userRole) {
        UserRole.CUSTOMER -> CustomerApp()
        UserRole.VENDOR -> VendorApp()
        UserRole.NONE ->
            LoginScreen(
                onLoginSuccess = { role ->
                    viewModel.updateUserRole(role)
                },
            )
    }
}
