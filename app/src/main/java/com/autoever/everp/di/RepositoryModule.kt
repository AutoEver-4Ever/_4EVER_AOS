package com.autoever.everp.di

import com.autoever.everp.data.repository.AlarmRepositoryImpl
import com.autoever.everp.data.repository.AuthRepositoryImpl
import com.autoever.everp.data.repository.DeviceInfoRepositoryImpl
import com.autoever.everp.data.repository.FcmRepositoryImpl
import com.autoever.everp.data.repository.MmRepositoryImpl
import com.autoever.everp.data.repository.ImRepositoryImpl
import com.autoever.everp.data.repository.PushNotificationRepositoryImpl
import com.autoever.everp.data.repository.SdRepositoryImpl
import com.autoever.everp.data.repository.UserRepositoryImpl
import com.autoever.everp.domain.repository.AlarmRepository
import com.autoever.everp.domain.repository.AuthRepository
import com.autoever.everp.domain.repository.DeviceInfoRepository
import com.autoever.everp.domain.repository.FcmRepository
import com.autoever.everp.domain.repository.MmRepository
import com.autoever.everp.domain.repository.ImRepository
import com.autoever.everp.domain.repository.PushNotificationRepository
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.domain.repository.UserRepository
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
    abstract fun bindsSdRepository(
        sdRepositoryImpl: SdRepositoryImpl
    ): SdRepository

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

    @Binds
    @Singleton
    abstract fun bindsAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl,
    ): AlarmRepository

    @Binds
    @Singleton
    abstract fun bindsFcmRepository(
        fcmRepositoryImpl: FcmRepositoryImpl,
    ): FcmRepository

    @Binds
    @Singleton
    abstract fun bindsMmRepository(
        mmRepositoryImpl: MmRepositoryImpl,
    ): MmRepository

    @Binds
    @Singleton
    abstract fun bindsImRepository(
        imRepositoryImpl: ImRepositoryImpl,
    ): ImRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsDashboardRepository(
        dashboardRepositoryImpl: com.autoever.everp.data.repository.DashboardRepositoryImpl,
    ): com.autoever.everp.domain.repository.DashboardRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepository(
        profileRepositoryImpl: com.autoever.everp.data.repository.ProfileRepositoryImpl,
    ): com.autoever.everp.domain.repository.ProfileRepository
}
