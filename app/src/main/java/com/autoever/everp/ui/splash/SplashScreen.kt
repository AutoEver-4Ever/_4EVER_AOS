package com.autoever.everp.ui.splash

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.autoever.everp.R
import com.autoever.everp.utils.permission.PermissionStatus
import com.autoever.everp.utils.permission.rememberPermissionState
import timber.log.Timber

@Composable
fun SplashScreen(
    onPermissionResult: (Boolean) -> Unit = {},
    onDialogStateChanged: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }

    // Dialog 상태가 변경될 때마다 콜백 호출
    LaunchedEffect(showPermissionDialog) {
        onDialogStateChanged(showPermissionDialog)
    }

    val (permissionStatus, requestPermission) = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS,
    ) { status ->
        when (status) {
            PermissionStatus.GRANTED -> {
                Timber.tag("SplashScreen").i("알림 권한이 부여되었습니다.")
                permissionDenied = false
                showPermissionDialog = false
                onPermissionResult(true)
            }
            PermissionStatus.DENIED -> {
                Timber.tag("SplashScreen").w("알림 권한이 거부되었습니다.")
                permissionDenied = true
                showPermissionDialog = true
            }
            PermissionStatus.NEEDS_RATIONALE -> {
                Timber.tag("SplashScreen").w("알림 권한 요청에 대한 설명이 필요합니다.")
                permissionDenied = true
                showPermissionDialog = true
            }
            PermissionStatus.UNKNOWN -> {
                Timber.tag("SplashScreen").d("알림 권한 상태를 확인할 수 없습니다.")
            }
        }
    }

    // SplashScreen이 표시될 때 권한 요청
    LaunchedEffect(Unit) {
        if (permissionStatus != PermissionStatus.GRANTED) {
            requestPermission()
        } else {
            onPermissionResult(true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.everp_logo),
            contentDescription = "EvERP 로고",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit,
        )
    }

    // 권한 거절 시 다시 요청하는 다이얼로그
    if (showPermissionDialog && permissionDenied) {
        AlertDialog(
            onDismissRequest = {
                showPermissionDialog = false
                onPermissionResult(false)
            },
            title = {
                Text("알림 권한이 필요합니다")
            },
            text = {
                Text(
                    "알림을 받으려면 알림 권한이 필요합니다.\n" +
                        "앱 설정에서 권한을 허용하거나, 다시 요청할 수 있습니다.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        // 앱 설정 화면으로 이동
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                            Timber.tag("SplashScreen").i("앱 설정 화면으로 이동했습니다.")
                        } catch (e: Exception) {
                            Timber.tag("SplashScreen").e(e, "앱 설정 화면으로 이동하는데 실패했습니다.")
                            // 설정 화면으로 이동할 수 없으면 다시 권한 요청
                            requestPermission()
                        }
                    },
                ) {
                    Text("설정으로 이동")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDialog = false
                        onPermissionResult(false)
                    },
                ) {
                    Text("나중에")
                }
            },
        )
    }
}

