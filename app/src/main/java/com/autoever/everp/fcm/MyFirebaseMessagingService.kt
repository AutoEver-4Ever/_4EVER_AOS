package com.autoever.everp.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.autoever.everp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * FCM 토큰이 갱신될 때 호출됩니다.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TAG).i("FCM new token: $token")
        // TODO: 서버로 토큰 업로드 API 호출
    }

    /**
     * FCM 메시지를 수신할 때 호출됩니다.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: message.data["title"] ?: "알림"
        val body = message.notification?.body ?: message.data["body"] ?: "새 알림이 있습니다."

        ensureNotificationChannel()

        // 권한 미허용 시 알림 표시하지 않음 (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val notification = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this)
            .notify(NotificationIdProvider.next(), notification)
    }

    companion object {
        private const val TAG = "FCMService"
        const val DEFAULT_CHANNEL_ID = "everp_default_notification"
    }

    private fun ensureNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val existed = manager.getNotificationChannel(DEFAULT_CHANNEL_ID) != null
            if (!existed) {
                val channel = NotificationChannel(
                    DEFAULT_CHANNEL_ID,
                    "일반 알림",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "EvERP 기본 알림 채널"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }
}

private object NotificationIdProvider {
    private val counter = AtomicInteger(0)
    fun next(): Int = counter.incrementAndGet()
}



