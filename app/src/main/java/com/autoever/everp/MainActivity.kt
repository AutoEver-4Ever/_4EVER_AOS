package com.autoever.everp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.autoever.everp.ui.navigation.AppNavGraph
import com.autoever.everp.ui.splash.SplashScreen
import com.autoever.everp.ui.theme.EverpTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EverpTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    var showSplash by remember { mutableStateOf(true) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var canProceed by remember { mutableStateOf(false) }

    // 2초 후 SplashScreen을 넘어갈 수 있는지 확인
    LaunchedEffect(Unit) {
        delay(2000) // 2초간 SplashScreen 표시
        canProceed = true
    }

    // Dialog가 닫혀있고 2초가 지났을 때만 다음 화면으로 이동
    LaunchedEffect(canProceed, isDialogOpen) {
        if (canProceed && !isDialogOpen && showSplash) {
            showSplash = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showSplash) {
            SplashScreen(
                onPermissionResult = { _ ->
                    // 권한 결과는 로깅용으로만 사용
                },
                onDialogStateChanged = { isOpen ->
                    isDialogOpen = isOpen
                    // Dialog가 닫혔고 2초가 지났으면 다음 화면으로 이동
                    if (!isOpen && canProceed) {
                        showSplash = false
                    }
                },
            )
        } else {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavGraph()
            }
        }
    }
}
