package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.AlarmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.AlarmApi
import com.autoever.everp.data.datasource.remote.http.service.AlarmTokenApi
import com.autoever.everp.data.datasource.remote.http.service.FcmTokenRegisterRequestDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationCountResponseDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationMarkReadRequestDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationReadResponseDto
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import timber.log.Timber
import javax.inject.Inject

/**
 * 알림 원격 데이터소스 구현체
 */
class AlarmHttpRemoteDataSourceImpl @Inject constructor(
    private val alarmApi: AlarmApi,
    private val alarmTokenApi: AlarmTokenApi,
) : AlarmRemoteDataSource {

    override suspend fun getNotificationList(
        sortBy: String,
        order: String,
        source: NotificationSourceEnum,
        page: Int,
        size: Int,
    ): Result<PageResponse<NotificationListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = alarmApi.getNotificationList(
                sortBy = sortBy.ifBlank { null },
                order = order.ifBlank { null },
                source = source.toApiString(),
                page = page,
                size = size,
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(
                    Exception(response.message ?: "알림 목록 조회 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "알림 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getNotificationCount(
        status: NotificationStatusEnum,
    ): Result<NotificationCountResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = alarmApi.getNotificationCount(
                status = status.toApiString(),
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(
                    Exception(response.message ?: "알림 개수 조회 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "알림 개수 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun markNotificationsAsRead(
        notificationIds: List<String>,
    ): Result<NotificationReadResponseDto> = withContext(Dispatchers.IO) {
        try {
            val request = NotificationMarkReadRequestDto(notificationIds = notificationIds)
            val response = alarmApi.markNotificationsAsRead(
                request,
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(
                    Exception(response.message ?: "알림 읽음 처리 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "알림 읽음 처리 실패")
            Result.failure(e)
        }
    }

    override suspend fun markAllNotificationsAsRead(

    ): Result<NotificationReadResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = alarmApi.markAllNotificationsAsRead()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(
                    Exception(response.message ?: "전체 알림 읽음 처리 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "전체 알림 읽음 처리 실패")
            Result.failure(e)
        }
    }

    override suspend fun markNotificationAsRead(
        notificationId: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = alarmApi.markNotificationAsRead(notificationId = notificationId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(response.message ?: "알림 읽음 처리 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "알림 읽음 처리 실패")
            Result.failure(e)
        }
    }

    override suspend fun registerFcmToken(
        token: String,
        deviceId: String,
        deviceType: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = FcmTokenRegisterRequestDto(
                token = token,
                deviceId = deviceId,
                deviceType = deviceType,
            )
            val response = alarmTokenApi.registerFcmToken(request)
            if (response.success) {
                Timber.d("FCM 토큰 등록 성공: ${response.data}")
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(response.message ?: "FCM 토큰 등록 실패"),
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "FCM 토큰 등록 실패")
            Result.failure(e)
        }
    }
}
