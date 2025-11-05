package com.autoever.everp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.autoever.everp.domain.repository.PushNotificationRepository
import com.autoever.everp.ui.MainScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.autoever.everp.ui.theme.EverpTheme
import com.autoever.everp.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var notificationRepository: PushNotificationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getFcmToken()
        setContent {
            EverpTheme {
                MainScreen()
//                Surface(modifier = Modifier.fillMaxSize()) {
//                    AppNavGraph()
//                }
            }
        }
    }

    private fun getFcmToken() {
        // Repository를 통해서만 FCM 토큰 접근
        // MainActivity에서는 Firebase 객체에 직접 접근하지 않음
        lifecycleScope.launch {
            try {
                val token = notificationRepository.getToken()
                Timber.tag("FCM").i("FCM Token: $token")
                // TODO: 서버에 토큰 전송 또는 로컬 저장 등 필요한 작업 수행
            } catch (e: Exception) {
                Timber.tag("FCM").e(e, "Fetching FCM token failed")
            }
        }
    }
}
