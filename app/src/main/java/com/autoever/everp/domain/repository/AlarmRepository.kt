package com.autoever.everp.domain.repository

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import com.autoever.everp.domain.model.notification.NotificationListParams
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import kotlinx.coroutines.flow.Flow

/**
 * 알림 Repository 인터페이스
 */
interface AlarmRepository {
    /**
     * 로컬 캐시를 관찰합니다.
     */
    fun observeNotifications(): Flow<PageResponse<Notification>>

    /**
     * 원격에서 가져와 로컬을 갱신합니다.
     */
    suspend fun refreshNotifications(
        params: NotificationListParams,
    ): Result<Unit>

    /**
     * 알림 목록 조회
     */
    suspend fun getNotificationList(
        params: NotificationListParams,
    ): Result<PageResponse<Notification>>

    /**
     * 알림 개수 로컬 캐시 관찰
     */
    fun observeNotificationCount(): Flow<NotificationCount>

    /**
     * 원격에서 개수 조회 후 로컬 갱신
     * 전체 개수와 읽지 않은 개수를 모두 조회하여 NotificationCount를 구성합니다.
     */
    suspend fun refreshNotificationCount(): Result<Unit>

    /**
     * 알림 개수 조회
     * 전체 개수와 읽지 않은 개수를 모두 조회하여 NotificationCount를 반환합니다.
     */
    suspend fun getNotificationCount(): Result<NotificationCount>

    /**
     * 알림 목록 읽음 처리
     */
    suspend fun markNotificationsAsRead(
        notificationIds: List<String>,
    ): Result<Int> // 처리된 개수

    /**
     * 모든 알림 읽음 처리
     */
    suspend fun markAllNotificationsAsRead(): Result<Int> // 처리된 개수

    /**
     * 특정 알림 읽음 처리
     */
    suspend fun markNotificationAsRead(
        notificationId: String,
    ): Result<Unit>

    /**
     * FCM 토큰 등록
     */
    suspend fun registerFcmToken(
        token: String,
        deviceId: String,
        deviceType: String = "ANDROID",
    )

    /**
     * 로컬에 저장된 FCM 토큰 관찰
     */
    fun observeToken(): Flow<String?>

    /**
     * 로컬 토큰 저장
     */
    suspend fun saveTokenLocally(token: String)

    /**
     * 로컬 토큰 삭제
     */
    suspend fun clearToken()
}
