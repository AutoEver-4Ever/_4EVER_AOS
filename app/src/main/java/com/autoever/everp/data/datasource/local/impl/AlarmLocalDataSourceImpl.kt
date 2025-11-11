package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.AlarmLocalDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageDto
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.NotificationListItemDto
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmLocalDataSourceImpl @Inject constructor() : AlarmLocalDataSource {
    private val notificationsFlow = MutableStateFlow(
        value = PageResponse.empty<Notification>()
    )

    private val countFlow = MutableStateFlow(
        NotificationCount(totalCount = 0, unreadCount = 0, readCount = 0),
    )

    override fun observeNotifications(): Flow<PageResponse<Notification>> = notificationsFlow.asStateFlow()

    override suspend fun setNotifications(page: PageResponse<Notification>) {
        notificationsFlow.value = page
    }

    override fun observeNotificationCount(): Flow<NotificationCount> = countFlow.asStateFlow()

    override suspend fun setNotificationCount(count: NotificationCount) {
        countFlow.value = count
    }
}
