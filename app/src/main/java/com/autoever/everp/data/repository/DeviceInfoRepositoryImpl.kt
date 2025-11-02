package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.DeviceLocalDataSource
import com.autoever.everp.domain.model.device.DeviceInfo
import com.autoever.everp.domain.repository.DeviceInfoRepository
import javax.inject.Inject

class DeviceInfoRepositoryImpl @Inject constructor(
    private val deviceLocalDataSource: DeviceLocalDataSource,
) : DeviceInfoRepository {

    override fun getAndroidId(): String {
        return deviceLocalDataSource.getAndroidId()
    }

    override fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            androidId = deviceLocalDataSource.getAndroidId(),
            manufacturer = deviceLocalDataSource.getManufacturer(),
            model = deviceLocalDataSource.getModel(),
            osVersion = deviceLocalDataSource.getOsVersion(),
            sdkVersion = deviceLocalDataSource.getSdkVersion(),
        )
    }
}
