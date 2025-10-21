package com.autoever.everp.utils.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat

/**
 * 권한 상태를 나타내는 열거형 클래스
 * - GRANTED: 권한이 부여된 상태
 * - DENIED: 권한이 거부된 상태
 * - NEEDS_RATIONALE: 권한 요청에 대한 설명이 필요한 상태
 * - UNKNOWN: 권한 상태를 알 수 없는 상태
 */
enum class PermissionStatus { GRANTED, DENIED, NEEDS_RATIONALE, UNKNOWN }

@Composable
fun rememberPermissionState(
    permission: String,
    onResult: (PermissionStatus) -> Unit,
): Pair<PermissionStatus, () -> Unit> {
    val context = LocalView.current.context
    val activity = context as? androidx.activity.ComponentActivity
    var permissionStatus by remember {
        mutableStateOf(checkInitialPermissionStatus(context, permission))
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                permissionStatus =
                    if (isGranted) {
                        PermissionStatus.GRANTED
                    } else {
                        val shouldShowRationale = activity?.shouldShowRequestPermissionRationale(permission) ?: false
                        if (shouldShowRationale) {
                            PermissionStatus.NEEDS_RATIONALE
                        } else {
                            PermissionStatus.DENIED
                        }
                    }
                onResult(permissionStatus)
            },
        )

    val requestPermission: () -> Unit = {
        permissionStatus = checkInitialPermissionStatus(context, permission) // 최신 상태로 갱신
        when (permissionStatus) {
            PermissionStatus.GRANTED -> {
                onResult(PermissionStatus.GRANTED)
            }

            else -> {
                if (activity?.shouldShowRequestPermissionRationale(permission) == true) {
                    // 권한 요청에 대한 설명이 필요한 경우
                    // 설명 다이얼로그 등은 UI 레이어에서 처리하도록 위임
                    permissionStatus = PermissionStatus.NEEDS_RATIONALE
                    onResult(permissionStatus)
                } else {
                    launcher.launch(permission) // 권한 요청 실행
                }
            }
        }
    }

    return Pair(permissionStatus, requestPermission)
}

/**
 * 현재 권한 상태를 확인
 */
private fun checkInitialPermissionStatus(
    context: Context,
    permission: String,
): PermissionStatus =
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        PermissionStatus.GRANTED
    } else {
        PermissionStatus.UNKNOWN
    }
