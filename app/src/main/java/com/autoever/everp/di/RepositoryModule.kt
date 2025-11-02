package com.autoever.everp.di

import com.autoever.everp.data.repository.DeviceInfoRepositoryImpl
import com.autoever.everp.data.repository.PushNotificationRepositoryImpl
import com.autoever.everp.data.repository.SdRepositoryImpl
import com.autoever.everp.domain.repository.DeviceInfoRepository
import com.autoever.everp.domain.repository.PushNotificationRepository
import com.autoever.everp.domain.repository.SdRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Application 범위에 설치
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsSdRepository(sdRepositoryImpl: SdRepositoryImpl): SdRepository

    @Binds
    @Singleton
    abstract fun bindsDeviceInfoRepository(
        deviceInfoRepositoryImpl: DeviceInfoRepositoryImpl,
    ): DeviceInfoRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(
        notificationRepositoryImpl: PushNotificationRepositoryImpl,
    ): PushNotificationRepository
}
