package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.AlarmLocalDataSource
import com.autoever.everp.data.datasource.remote.AlarmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.mapper.NotificationMapper
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import com.autoever.everp.domain.model.notification.NotificationListParams
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import com.autoever.everp.domain.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * 알림 Repository 구현체
 */
class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {

    override fun observeNotifications(): Flow<PageResponse<Notification>> =
        alarmLocalDataSource.observeNotifications()

    override suspend fun refreshNotifications(
        params: NotificationListParams,
    ): Result<Unit> = withContext(Dispatchers.Default) {
        getNotificationList(params).map { page ->
            alarmLocalDataSource.setNotifications(page)
        }
    }

    override suspend fun getNotificationList(
        params: NotificationListParams,
    ): Result<PageResponse<Notification>> = withContext(Dispatchers.Default) {
        alarmRemoteDataSource.getNotificationList(
            sortBy = params.sortBy,
            order = params.order,
            source = params.source,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            // DTO를 Domain Model로 변환
            PageResponse(
                content = NotificationMapper.toDomainList(dtoPage.content),
                page = dtoPage.page, // PageDto 그대로 전달
            )
        }
    }

    override fun observeNotificationCount(): Flow<NotificationCount> =
        alarmLocalDataSource.observeNotificationCount()

    override suspend fun refreshNotificationCount(): Result<Unit> {
        return getNotificationCount().map { count ->
            alarmLocalDataSource.setNotificationCount(count)
        }
    }

    override suspend fun getNotificationCount(

    ): Result<NotificationCount> = withContext(Dispatchers.Default) {
        // 1. 두 개의 작업을 'async'로 동시에 시작
        val totalResultAsync = async {
            alarmRemoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNKNOWN)
        }
        val unreadResultAsync = async {
            alarmRemoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNREAD)
        }

        // 2. 두 작업의 결과를 'await'로 수집
        val totalResult = totalResultAsync.await()
        val unreadResult = unreadResultAsync.await()

        // 3. 두 Result를 'runCatching'으로 안전하게 조합
        runCatching {
            val totalDto = totalResult.getOrThrow() // totalResult가 Failure라면 여기서 예외가 발생

            val unreadDto = unreadResult.getOrThrow() // unreadResult가 Failure라면 여기서 예외가 발생

            // 두 DTO를 성공적으로 가져온 경우에만 실행됨
            NotificationCount(
                totalCount = totalDto.count,
                unreadCount = unreadDto.count,
                readCount = totalDto.count - unreadDto.count,
            )
        } // runCatching 블록이 발생한 예외를 잡아 Result.failure로 반환해줌
    }

    override suspend fun markNotificationsAsRead(
        notificationIds: List<String>,
    ): Result<Int> {
        return alarmRemoteDataSource.markNotificationsAsRead(notificationIds)
            .map { it.updatedCount }
            .onSuccess {
                // 읽음 처리 성공 시 로컬 캐시 업데이트
                updateLocalNotificationsAsRead(notificationIds)
            }
    }

    override suspend fun markAllNotificationsAsRead(): Result<Int> {
        return alarmRemoteDataSource.markAllNotificationsAsRead()
            .map { it.updatedCount }
            .onSuccess {
                // 전체 읽음 처리 성공 시 로컬 캐시 업데이트
                markAllLocalNotificationsAsRead()
            }
    }

    override suspend fun markNotificationAsRead(
        notificationId: String,
    ): Result<Unit> {
        return alarmRemoteDataSource.markNotificationAsRead(notificationId)
            .onSuccess {
                // 읽음 처리 성공 시 로컬 캐시 업데이트
                updateLocalNotificationsAsRead(listOf(notificationId))
            }
    }

    /**
     * 로컬 캐시의 알림을 읽음 상태로 업데이트
     */
    private suspend fun updateLocalNotificationsAsRead(notificationIds: List<String>) {
        val current = alarmLocalDataSource.observeNotifications()
        // 현재 Flow 값을 업데이트하려면 StateFlow에서 value를 가져와 수정 필요
        // 간단한 구현을 위해 전체 리프레시 권장
    }

    /**
     * 로컬 캐시의 모든 알림을 읽음 상태로 업데이트
     */
    private suspend fun markAllLocalNotificationsAsRead() {
        // 전체 리프레시 권장
    }

    override suspend fun registerFcmToken(
        token: String,
        deviceId: String,
        deviceType: String,
    ) {
        alarmRemoteDataSource.registerFcmToken(
            token = token,
            deviceId = deviceId,
            deviceType = deviceType,
        ).onSuccess {
            // 서버 등록 성공 시 로컬에도 저장
//            alarmLocalDataSource.saveToken(token)
        }
    }

    override fun observeToken(): Flow<String?> {
//        return alarmLocalDataSource.observeToken()
        return MutableStateFlow<String>(value = "")
    }

    override suspend fun saveTokenLocally(token: String) {
//        alarmLocalDataSource.saveToken(token)
    }

    override suspend fun clearToken() {
//        alarmLocalDataSource.clearToken()
    }
}
