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

    LaunchedEffect(Unit) {
        delay(2000) // 2초간 SplashScreen 표시
        showSplash = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showSplash) {
            SplashScreen()
        } else {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavGraph()
            }
        }
    }
}
