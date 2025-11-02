package com.autoever.everp.data.datasource.remote

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase Crashlytics 사용 예시를 위한 DataSource
 * 실제 사용 시에는 Repository 패턴으로 구현하는 것을 권장합니다.
 */
@Singleton
class FirebaseCrashlyticsDataSource @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
) {
    /**
     * 예외를 Crashlytics에 기록합니다.
     * @param throwable 기록할 예외
     */
    fun recordException(throwable: Throwable) {
        firebaseDataSource.crashlytics.recordException(throwable)
    }

    /**
     * 커스텀 키를 설정합니다.
     * @param key 키 이름
     * @param value 키 값
     */
    fun setCustomKey(key: String, value: String) {
        firebaseDataSource.crashlytics.setCustomKey(key, value)
    }

    /**
     * 커스텀 키를 설정합니다 (Int 타입).
     */
    fun setCustomKey(key: String, value: Int) {
        firebaseDataSource.crashlytics.setCustomKey(key, value)
    }

    /**
     * 로그 메시지를 기록합니다.
     * @param message 로그 메시지
     */
    fun log(message: String) {
        firebaseDataSource.crashlytics.log(message)
    }
}

