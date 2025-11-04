package com.autoever.everp.data.datasource.local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /**
     * Android ID를 가져옵니다.
     * 앱이 삭제되고 재설치되어도 동일한 값이 유지됩니다.
     */
    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: ""
    }

    /**
     * 기기 제조사 이름을 가져옵니다.
     */
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 기기 모델 이름을 가져옵니다.
     */
    fun getModel(): String {
        return Build.MODEL
    }

    /**
     * OS 버전을 가져옵니다.
     */
    fun getOsVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * SDK 버전을 가져옵니다.
     */
    fun getSdkVersion(): Int {
        return Build.VERSION.SDK_INT
    }
}
