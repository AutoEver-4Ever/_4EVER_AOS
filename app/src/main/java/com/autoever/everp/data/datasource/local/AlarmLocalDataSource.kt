package com.autoever.everp.data.datasource.local

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import kotlinx.coroutines.flow.Flow

interface AlarmLocalDataSource {
    fun observeNotifications(): Flow<PageResponse<Notification>>
    suspend fun setNotifications(page: PageResponse<Notification>)

    fun observeNotificationCount(): Flow<NotificationCount>
    suspend fun setNotificationCount(count: NotificationCount)
}
