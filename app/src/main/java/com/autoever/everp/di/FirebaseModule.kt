package com.autoever.everp.di

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Firebase 서비스 객체들을 Hilt로 관리하는 Module
 * 모든 Firebase 객체는 싱글톤으로 제공되어 하나의 인스턴스만 사용됩니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * FirebaseMessaging 싱글톤 인스턴스를 제공합니다.
     * FirebaseMessaging.getInstance()는 내부적으로 싱글톤을 보장하지만,
     * Hilt를 통해 명시적으로 관리합니다.
     */
    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    /**
     * FirebaseAnalytics 싱글톤 인스턴스를 제공합니다.
     * 사용자 이벤트 추적 및 분석에 사용됩니다.
     */
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application)
    }

    /**
     * FirebaseCrashlytics 싱글톤 인스턴스를 제공합니다.
     * 크래시 리포팅 및 분석에 사용됩니다.
     */
    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    /**
     * FirebaseRemoteConfig 싱글톤 인스턴스를 제공합니다.
     * 원격 설정 관리에 사용됩니다.
     */
    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(application: Application): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance(application)
    }
}
