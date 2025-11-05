package com.autoever.everp.data.datasource.remote.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase 서비스 객체들을 중앙에서 관리하는 DataSource
 * Firebase SDK는 내부적으로 싱글톤이지만, 아키텍처 일관성을 위해 여기서 관리합니다.
 *
 * 관리되는 Firebase 객체:
 * - FirebaseMessaging: FCM 푸시 알림
 * - FirebaseAnalytics: 사용자 이벤트 추적 및 분석
 * - FirebaseCrashlytics: 크래시 리포팅 및 분석
 * - FirebaseRemoteConfig: 원격 설정 관리
 */
@Singleton
class FirebaseDataSource @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) {
    /**
     * FCM 토큰 관리 및 메시징 기능
     * 사용 예: firebaseDataSource.messaging.token
     */
    val messaging: FirebaseMessaging = firebaseMessaging

    /**
     * Firebase Analytics - 이벤트 추적 및 분석
     * 사용 예: firebaseDataSource.analytics.logEvent("event_name", bundle)
     */
    val analytics: FirebaseAnalytics = firebaseAnalytics

    /**
     * Firebase Crashlytics - 크래시 리포팅
     * 사용 예: firebaseDataSource.crashlytics.recordException(exception)
     */
    val crashlytics: FirebaseCrashlytics = firebaseCrashlytics

    /**
     * Firebase Remote Config - 원격 설정 관리
     * 사용 예: firebaseDataSource.remoteConfig.getString("config_key")
     */
    val remoteConfig: FirebaseRemoteConfig = firebaseRemoteConfig
}
