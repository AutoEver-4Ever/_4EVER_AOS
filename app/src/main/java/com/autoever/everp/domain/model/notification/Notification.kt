package com.autoever.everp.domain.model.notification

import java.time.LocalDateTime

/**
 * 알림 Domain Model
 */
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val linkType: NotificationLinkEnum,
    val linkId: String?,
    val source: NotificationSourceEnum,
    val status: NotificationStatusEnum,
    val createdAt: LocalDateTime,
) {
    /**
     * 읽음 상태 확인
     */
    val isRead: Boolean
        get() = status == NotificationStatusEnum.READ

    /**
     * 화면 이동 가능 여부
     */
    val isNavigable: Boolean
        get() = linkType.hasNavigation() && linkId != null
}

/**
 * 알림 개수 Domain Model
 */
data class NotificationCount(
    val totalCount: Int,
    val unreadCount: Int,
    val readCount: Int,
) {
    /**
     * 읽지 않은 알림이 있는지 확인
     */
    val hasUnread: Boolean
        get() = unreadCount > 0
}
