package com.autoever.everp.service.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.autoever.everp.R
import com.autoever.everp.domain.repository.AlarmRepository
import com.autoever.everp.domain.repository.DeviceInfoRepository
import com.autoever.everp.domain.repository.PushNotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deviceInfoRepository: DeviceInfoRepository

    @Inject
    lateinit var pushNotificationRepository: PushNotificationRepository

    @Inject
    lateinit var alarmRepository: AlarmRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * FCM 토큰이 갱신될 때 호출됩니다.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag(TAG).i("FCM new token: $token")

        // 새 토큰을 서버에 등록
        registerFcmToken(token)
    }

    /**
     * FCM 토큰을 서버에 등록합니다.
     */
    private fun registerFcmToken(token: String) {
        serviceScope.launch {
            try {
                // Android ID 가져오기
                val androidId = deviceInfoRepository.getAndroidId()
                Timber.tag(TAG).d("[INFO] Android ID 획득: $androidId")

                // 서버에 FCM 토큰 등록
                alarmRepository.registerFcmToken(
                    token = token,
                    deviceId = androidId,
                    deviceType = "ANDROID",
                )
                Timber.tag(TAG).i("[INFO] FCM 토큰 서버 등록 완료")
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "[ERROR] FCM 토큰 등록 실패: ${e.message}")
                // FCM 토큰 등록 실패는 치명적이지 않으므로 로그만 남기고 계속 진행
            }
        }
    }

    /**
     * FCM 메시지를 수신할 때 호출됩니다.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // 상세 로그 출력
        Timber.tag(TAG).d("========== FCM 메시지 수신 ==========")
        Timber.tag(TAG).d("Message ID: ${message.messageId}")
        Timber.tag(TAG).d("From: ${message.from}")
        Timber.tag(TAG).d("Message Type: ${message.messageType}")
        Timber.tag(TAG).d("Collapse Key: ${message.collapseKey}")
        Timber.tag(TAG).d("Sent Time: ${message.sentTime}")
        Timber.tag(TAG).d("TTL: ${message.ttl}")

        // Notification 데이터
        message.notification?.let { notification ->
            Timber.tag(TAG).d("--- Notification Payload ---")
            Timber.tag(TAG).d("Title: ${notification.title}")
            Timber.tag(TAG).d("Body: ${notification.body}")
            Timber.tag(TAG).d("Icon: ${notification.icon}")
            Timber.tag(TAG).d("Sound: ${notification.sound}")
            Timber.tag(TAG).d("Tag: ${notification.tag}")
            Timber.tag(TAG).d("Color: ${notification.color}")
            Timber.tag(TAG).d("Click Action: ${notification.clickAction}")
        }

        // Data 페이로드
        if (message.data.isNotEmpty()) {
            Timber.tag(TAG).d("--- Data Payload ---")
            message.data.forEach { (key, value) ->
                Timber.tag(TAG).d("  $key: $value")
            }
        } else {
            Timber.tag(TAG).d("Data payload: 없음")
        }

        val title = message.notification?.title ?: message.data["title"] ?: "알림"
        val body = message.notification?.body ?: message.data["body"] ?: "새 알림이 있습니다."

        Timber.tag(TAG).i("알림 표시 - Title: $title, Body: $body")

        // NotificationChannel 생성 확인
        ensureNotificationChannel()

        // 채널이 활성화되어 있는지 확인 (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val channel = manager.getNotificationChannel(DEFAULT_CHANNEL_ID)
            if (channel == null) {
                Timber.tag(TAG).e("NotificationChannel이 생성되지 않았습니다!")
                return
            }
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                Timber.tag(TAG).w("NotificationChannel이 비활성화되어 있습니다.")
                return
            }
            Timber.tag(TAG).d("NotificationChannel 확인 - ID: ${channel.id}, Importance: ${channel.importance}")
        }

        // 권한 미허용 시 알림 표시하지 않음 (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                Timber.tag(TAG).w("POST_NOTIFICATIONS 권한이 없어 알림을 표시하지 않습니다.")
                return
            }
        }

        // 알림 빌더 생성
        val notification = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // TODO: 알림용 작은 아이콘으로 교체 권장
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // 소리, 진동, 불빛 등 기본 설정
            .build()

        // 알림 표시
        val notificationId = NotificationIdProvider.next()
        try {
            val notificationManager = NotificationManagerCompat.from(this)

            // 알림 표시 가능 여부 확인
            if (!notificationManager.areNotificationsEnabled()) {
                Timber.tag(TAG).w("시스템 설정에서 알림이 비활성화되어 있습니다.")
                return
            }

            notificationManager.notify(notificationId, notification)

            Timber.tag(TAG).i("✅ 알림 표시 완료 (Notification ID: $notificationId)")
            Timber.tag(TAG).d("========================================")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "❌ 알림 표시 실패 (Notification ID: $notificationId)")
        }
    }

    /**
     * 앱이 백그라운드에 있을 때 알림이 수신되면 호출됩니다.
     * 이 경우 시스템이 자동으로 알림을 표시합니다.
     */
    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Timber.tag(TAG).d("메시지 전송 완료: $msgId")
    }

    /**
     * 메시지 전송 실패 시 호출됩니다.
     */
    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Timber.tag(TAG).e(exception, "메시지 전송 실패: $msgId")
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

    override fun onDestroy() {
        super.onDestroy()
        // 2. 서비스가 종료될 때 스코프를 반드시 취소! -> 메모리 누수 방지
        serviceScope.cancel()
    }

}

private object NotificationIdProvider {
    private val counter = AtomicInteger(0)
    fun next(): Int = counter.incrementAndGet()
}



