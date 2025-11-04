package com.autoever.everp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import com.autoever.everp.service.fcm.MyFirebaseMessagingService
import timber.log.Timber

@HiltAndroidApp
class EverpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        createNotificationChannels()
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
