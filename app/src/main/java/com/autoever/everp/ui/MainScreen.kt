package com.autoever.everp.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.domain.model.user.UserTypeEnum
import com.autoever.everp.ui.customer.CustomerApp
import com.autoever.everp.ui.login.LoginScreen
import com.autoever.everp.ui.supplier.SupplierApp
import com.autoever.everp.utils.permission.PermissionStatus
import com.autoever.everp.utils.permission.rememberPermissionState
import timber.log.Timber

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    // ViewModel로부터 사용자 역할 상태를 관찰
    val userRole by viewModel.userRole.collectAsState()

    val (permissionStatus, requestPermission) = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    ) { status ->
        when (status) {
            PermissionStatus.GRANTED -> Timber.tag("TAG").i("Permission Granted")
            PermissionStatus.DENIED -> Timber.tag("TAG").i("Permission Denied")
            PermissionStatus.NEEDS_RATIONALE -> Timber.tag("TAG").i("Permission Needs_rationale")
            PermissionStatus.UNKNOWN -> TODO()
        }
    }

    LaunchedEffect(Unit) {
        if (permissionStatus != PermissionStatus.GRANTED) {
            requestPermission()
        }
    }

    // 역할 상태에 따라 적절한 UI를 렌더링
    when (userRole) {
        UserTypeEnum.CUSTOMER -> CustomerApp(navController)
        UserTypeEnum.SUPPLIER -> SupplierApp(navController)
        else -> LoginScreen {
//            onLoginSuccess = { role ->
//                viewModel.updateUserRole(role)
//            },
        }
    }
}
