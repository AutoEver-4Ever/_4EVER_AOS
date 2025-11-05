package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.device.DeviceInfo

/**
 * 기기 정보를 관리하는 Repository 인터페이스
 */
interface DeviceInfoRepository {
    /**
     * Android ID를 가져옵니다.
     */
    fun getAndroidId(): String

    /**
     * 기기 정보를 종합하여 가져옵니다.
     */
    fun getDeviceInfo(): DeviceInfo
}
