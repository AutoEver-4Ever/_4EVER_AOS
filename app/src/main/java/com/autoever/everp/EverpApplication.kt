package com.autoever.everp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.autoever.everp.common.exception.GlobalExceptionHandler
import dagger.hilt.android.HiltAndroidApp
import com.autoever.everp.service.fcm.MyFirebaseMessagingService
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class EverpApplication : Application() {

    @Inject
    lateinit var globalExceptionHandler: GlobalExceptionHandler

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initGlobalExceptionHandler()
        createNotificationChannels()
    }

    /**
     * 전역 예외 처리기 초기화
     * 
     * 가장 먼저 초기화하여 앱 실행 중 발생하는 모든 예외를 잡을 수 있도록 함
     */
    private fun initGlobalExceptionHandler() {
        try {
            globalExceptionHandler.initialize()
            Timber.i("Global exception handler initialized")
        } catch (e: Exception) {
            // 초기화 실패 시에도 앱은 계속 실행
            Timber.e(e, "Failed to initialize global exception handler")
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MyFirebaseMessagingService.DEFAULT_CHANNEL_ID,
                "일반 알림",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = "EvERP 기본 알림 채널"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
