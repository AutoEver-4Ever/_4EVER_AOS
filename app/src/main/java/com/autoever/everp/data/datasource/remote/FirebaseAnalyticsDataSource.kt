package com.autoever.everp.data.datasource.remote

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase Analytics 사용 예시를 위한 DataSource
 * 실제 사용 시에는 Repository 패턴으로 구현하는 것을 권장합니다.
 */
@Singleton
class FirebaseAnalyticsDataSource @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
) {
    /**
     * 커스텀 이벤트를 로깅합니다.
     * @param eventName 이벤트 이름
     * @param params 이벤트 파라미터 (null 가능)
     */
    fun logEvent(eventName: String, params: Bundle? = null) {
        firebaseDataSource.analytics.logEvent(eventName, params)
    }

    /**
     * 화면 조회를 로깅합니다.
     * @param screenName 화면 이름
     * @param screenClass 화면 클래스 이름
     */
    fun logScreenView(screenName: String, screenClass: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
        firebaseDataSource.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    /**
     * 사용자 속성을 설정합니다.
     * @param name 속성 이름
     * @param value 속성 값
     */
    fun setUserProperty(name: String, value: String) {
        firebaseDataSource.analytics.setUserProperty(name, value)
    }
}

