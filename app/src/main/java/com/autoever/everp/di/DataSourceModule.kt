package com.autoever.everp.di

import com.autoever.everp.data.datasource.local.AlarmLocalDataSource
import com.autoever.everp.data.datasource.local.AuthLocalDataSource
import com.autoever.everp.data.datasource.local.FcmLocalDataSource
import com.autoever.everp.data.datasource.local.MmLocalDataSource
import com.autoever.everp.data.datasource.local.SdLocalDataSource
import com.autoever.everp.data.datasource.local.TokenLocalDataSource
import com.autoever.everp.data.datasource.local.UserLocalDataSource
import com.autoever.everp.data.datasource.local.datastore.TokenDataStoreLocalDataSourceImpl
import com.autoever.everp.data.datasource.local.impl.AlarmLocalDataSourceImpl
import com.autoever.everp.data.datasource.local.impl.FcmLocalDataSourceImpl
import com.autoever.everp.data.datasource.local.impl.MmLocalDataSourceImpl
import com.autoever.everp.data.datasource.local.impl.SdLocalDataSourceImpl
import com.autoever.everp.data.datasource.local.impl.UserLocalDataSourceImpl
import com.autoever.everp.data.datasource.remote.AlarmRemoteDataSource
import com.autoever.everp.data.datasource.remote.AuthRemoteDataSource
import com.autoever.everp.data.datasource.remote.FcmRemoteDataSource
import com.autoever.everp.data.datasource.remote.MmRemoteDataSource
import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.UserRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.impl.AlarmHttpRemoteDataSourceImpl
import com.autoever.everp.data.datasource.remote.http.impl.FcmHttpRemoteDataSourceImpl
import com.autoever.everp.data.datasource.remote.http.impl.MmHttpRemoteDataSourceImpl
import com.autoever.everp.data.datasource.remote.http.impl.SdHttpRemoteDataSourceImpl
import com.autoever.everp.data.datasource.remote.http.impl.UserHttpRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Application 범위에 설치
abstract class DataSourceModule {

    // Alarm Data Sources
    @Binds
    @Singleton
    abstract fun bindsAlarmRemoteDataSource(
        alarmHttpRemoteDataSourceImpl: AlarmHttpRemoteDataSourceImpl,
    ): AlarmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsAlarmLocalDataSource(
        alarmLocalDataSourceImpl: AlarmLocalDataSourceImpl,
    ): AlarmLocalDataSource

    // Fcm Data Sources
    @Binds
    @Singleton
    abstract fun bindsFcmRemoteDataSource(
        fcmHttpRemoteDataSourceImpl: FcmHttpRemoteDataSourceImpl,
    ): FcmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsFcmLocalDataSource(
        fcmLocalDataSourceImpl: FcmLocalDataSourceImpl,
    ): FcmLocalDataSource

    // Mm Data Sources
    @Binds
    @Singleton
    abstract fun bindsMmRemoteDataSource(
        mmHttpRemoteDataSourceImpl: MmHttpRemoteDataSourceImpl,
    ): MmRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsMmLocalDataSource(
        mmLocalDataSourceImpl: MmLocalDataSourceImpl,
    ): MmLocalDataSource

    // User Data Sources
    @Binds
    @Singleton
    abstract fun bindsUserRemoteDataSource(
        userHttpRemoteDataSourceImpl: UserHttpRemoteDataSourceImpl,
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl,
    ): UserLocalDataSource

    // Sd Data Sources
    @Binds
    @Singleton
    abstract fun bindsSdRemoteDataSource(
        sdHttpRemoteDataSourceImpl: SdHttpRemoteDataSourceImpl,
    ): SdRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsSdLocalDataSource(
        sdLocalDataSourceImpl: SdLocalDataSourceImpl,
    ): SdLocalDataSource

    // Auth Data Sources
    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(
        authHttpRemoteDataSourceImpl: AlarmHttpRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsAuthLocalDataSource(
        authDataStoreLocalDataSourceImpl: AlarmHttpRemoteDataSourceImpl
    ): AuthLocalDataSource

    // Token Data Sources (AccessToken, FcmToken)
    @Binds
    @Singleton
    abstract fun bindsTokenLocalDataSource(
        tokenDataStoreLocalDataSourceImpl: TokenDataStoreLocalDataSourceImpl
    ): TokenLocalDataSource
}
