package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.notification.NotificationLinkEnum
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*

/**
 * 알림 관련 API Service
 * Base URL: /alarm/notifications
 */
interface AlarmApi {

    /**
     * 알림 목록 조회
     */
    @GET("$BASE_URL/list")
    suspend fun getNotificationList(
        @Query("sortBy") sortBy: String? = null, // createdAt, etc.
        @Query("order") order: String? = null, // asc, desc
        @Query("source") source: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<NotificationListItemDto>>

    /**
     * 알림 개수 조회
     */
    @GET("$BASE_URL/count")
    suspend fun getNotificationCount(
        @Query("status") status: String? = null, // READ, UNREAD
    ): ApiResponse<NotificationCountResponseDto>

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
@Serializable
data class NotificationListItemDto(
    @SerialName("notificationId")
    val notificationId: String,
    @SerialName("notificationTitle")
    val title: String,
    @SerialName("notificationMessage")
    val message: String,
    @SerialName("linkType")
    val linkType: NotificationLinkEnum,
    @SerialName("linkId")
    val linkId: String,
    @SerialName("source")
    val source: String,
    @SerialName("status")
    val status: String,
    @SerialName("createdAt")
    val createdAt: String,
)

@Serializable
data class NotificationCountResponseDto(
    @SerialName("count")
    val count: Int
//    val totalCount: Int,
//    val unreadCount: Int,
//    val readCount: Int,
)

@Serializable
data class NotificationMarkReadRequestDto(
    @SerialName("notificationIds")
    val notificationIds: List<String>,
)

@Serializable
data class NotificationReadResponseDto(
    @SerialName("processedCount")
    val updatedCount: Int,
)

