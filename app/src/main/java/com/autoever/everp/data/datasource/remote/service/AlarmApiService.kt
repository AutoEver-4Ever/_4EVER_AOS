package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import retrofit2.http.*

/**
 * 알림 관련 API Service
 * Base URL: /alarm/notifications
 */
interface AlarmApiService {

    /**
     * 알림 목록 조회
     */
    @GET("$BASE_URL/list")
    suspend fun getNotificationList(
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("order") order: String = "desc",
        @Query("source") source: String? = null, // PR, SD, IM, FCM, HRM, PP, CUS, SUP
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<NotificationListItemDto>>

    /**
     * 알림 개수 조회
     */
    @GET("$BASE_URL/count")
    suspend fun getNotificationCount(
        @Query("status") status: String? = null, // READ, UNREAD
    ): ApiResponse<NotificationCountDto>

    /**
     * SSE 구독 (Android에서는 일반적으로 사용 안 함)
     */
    @POST("$BASE_URL/subscribe/{userId}")
    suspend fun subscribeNotifications(
        @Path("userId") userId: String,
    ): ApiResponse<Map<String, Any>>

    /**
     * 알림 목록 읽음 처리
     */
    @PATCH("$BASE_URL/list/read")
    suspend fun markNotificationsAsRead(
        @Body request: NotificationMarkReadRequestDto,
    ): ApiResponse<NotificationReadResponseDto>

    /**
     * 모든 알림 읽음 처리
     */
    @PATCH("$BASE_URL/all/read")
    suspend fun markAllNotificationsAsRead(): ApiResponse<NotificationReadResponseDto>

    /**
     * 특정 알림 읽음 처리
     */
    @PATCH("$BASE_URL/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String,
    ): ApiResponse<Unit>

    companion object {
        private const val BASE_URL = "/alarm/notifications"
    }
}

// DTO 정의
@kotlinx.serialization.Serializable
data class NotificationListItemDto(
    val notificationId: String,
    val title: String,
    val message: String,
    val source: String,
    val status: String,
    val createdAt: String,
)

@kotlinx.serialization.Serializable
data class NotificationCountDto(
    val totalCount: Int,
    val unreadCount: Int,
    val readCount: Int,
)

@kotlinx.serialization.Serializable
data class NotificationMarkReadRequestDto(
    val notificationId: List<String>,
)

@kotlinx.serialization.Serializable
data class NotificationReadResponseDto(
    val updatedCount: Int,
)

