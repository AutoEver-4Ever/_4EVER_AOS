package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.NotificationCountResponseDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationListItemDto
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import java.time.LocalDateTime

/**
 * Notification DTO to Domain Model Mapper
 */
object NotificationMapper {

    /**
     * NotificationListItemDto를 Notification Domain Model로 변환
     */
    fun toDomain(dto: NotificationListItemDto): Notification {
        val source = NotificationSourceEnum.fromStringOrNull(dto.source)
            ?: NotificationSourceEnum.UNKNOWN

        val status = dto.isRead.let { if (it) NotificationStatusEnum.READ else NotificationStatusEnum.UNREAD }

        // createdAt 파싱 (ISO 8601 형식 가정)
        val createdAt = try {
            LocalDateTime.parse(dto.createdAt, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: Exception) {
            // 다른 형식 시도
            try {
                LocalDateTime.parse(dto.createdAt, java.time.format.DateTimeFormatter.ISO_DATE_TIME)
            } catch (e2: Exception) {
                LocalDateTime.now() // 파싱 실패 시 현재 시간
            }
        }

        return Notification(
            id = dto.notificationId,
            title = dto.title,
            message = dto.message,
            linkType = dto.linkType,
            linkId = dto.linkId.takeIf { it.isNotBlank() },
            source = source,
            status = status,
            createdAt = createdAt,
        )
    }

    /**
     * NotificationCountResponseDto를 NotificationCount Domain Model로 변환
     */
    fun toDomain(dto: NotificationCountResponseDto): NotificationCount {
        // TODO: API 응답 구조에 맞게 수정 필요
        // 현재는 count만 있으므로 임시로 처리
        return NotificationCount(
            totalCount = dto.count,
            unreadCount = dto.count, // TODO: 실제 unreadCount 받아서 수정
            readCount = 0, // TODO: 실제 readCount 받아서 수정
        )
    }

    /**
     * List<NotificationListItemDto>를 List<Notification>로 변환
     */
    fun toDomainList(dtoList: List<NotificationListItemDto>): List<Notification> {
        return dtoList.map { toDomain(it) }
    }
}

