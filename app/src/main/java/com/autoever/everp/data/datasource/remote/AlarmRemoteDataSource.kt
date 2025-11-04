package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.NotificationCountResponseDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationReadResponseDto
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.model.notification.NotificationStatusEnum

/**
 * 알림 원격 데이터소스 인터페이스
 */
interface AlarmRemoteDataSource {
    /**
     * 알림 목록 조회
     */
    suspend fun getNotificationList(
        sortBy: String = "",  // createdAt, etc.
        order: String = "",  // asc, desc
        source: NotificationSourceEnum = NotificationSourceEnum.UNKNOWN,
        page: Int = 0,
        size: Int = 20,
    ): Result<PageResponse<NotificationListItemDto>>

    /**
     * 알림 개수 조회
     */
    suspend fun getNotificationCount(
        status: NotificationStatusEnum = NotificationStatusEnum.UNKNOWN,
    ): Result<NotificationCountResponseDto>

    /**
     * 알림 목록 읽음 처리
     */
    suspend fun markNotificationsAsRead(
        notificationIds: List<String>,
    ): Result<NotificationReadResponseDto>

    /**
     * 모든 알림 읽음 처리
     */
    suspend fun markAllNotificationsAsRead(): Result<NotificationReadResponseDto>

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
    ): Result<Unit>
}
