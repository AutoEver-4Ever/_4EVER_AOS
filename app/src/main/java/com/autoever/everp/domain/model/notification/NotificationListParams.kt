package com.autoever.everp.domain.model.notification

/**
 * 알림 목록 조회 파라미터
 */
data class NotificationListParams(
    val sortBy: String = "", // createdAt, etc.
    val order: String = "", // asc, desc
    val source: NotificationSourceEnum = NotificationSourceEnum.UNKNOWN,
    val page: Int = 0,
    val size: Int = 20,
)
