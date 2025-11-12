package com.autoever.everp.domain.model.notification

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

/**
 * Notification Domain Model 테스트
 */
class NotificationTest {

    @Test
    fun `isRead는 READ 상태일 때 true를 반환해야 함`() {
        // Given
        val notification = Notification(
            id = "1",
            title = "Test",
            message = "Test",
            linkType = NotificationLinkEnum.QUOTATION,
            linkId = "1",
            source = NotificationSourceEnum.SD,
            status = NotificationStatusEnum.READ,
            createdAt = LocalDateTime.now(),
        )

        // When & Then
        assertTrue(notification.isRead)
    }

    @Test
    fun `isRead는 UNREAD 상태일 때 false를 반환해야 함`() {
        // Given
        val notification = Notification(
            id = "1",
            title = "Test",
            message = "Test",
            linkType = NotificationLinkEnum.QUOTATION,
            linkId = "1",
            source = NotificationSourceEnum.SD,
            status = NotificationStatusEnum.UNREAD,
            createdAt = LocalDateTime.now(),
        )

        // When & Then
        assertFalse(notification.isRead)
    }

    @Test
    fun `isNavigable는 linkType이 navigation을 지원하고 linkId가 있을 때 true를 반환해야 함`() {
        // Given
        val notification = Notification(
            id = "1",
            title = "Test",
            message = "Test",
            linkType = NotificationLinkEnum.QUOTATION,
            linkId = "quote1",
            source = NotificationSourceEnum.SD,
            status = NotificationStatusEnum.UNREAD,
            createdAt = LocalDateTime.now(),
        )

        // When & Then
        assertTrue(notification.isNavigable)
    }

    @Test
    fun `isNavigable는 linkId가 null일 때 false를 반환해야 함`() {
        // Given
        val notification = Notification(
            id = "1",
            title = "Test",
            message = "Test",
            linkType = NotificationLinkEnum.QUOTATION,
            linkId = null,
            source = NotificationSourceEnum.SD,
            status = NotificationStatusEnum.UNREAD,
            createdAt = LocalDateTime.now(),
        )

        // When & Then
        assertFalse(notification.isNavigable)
    }
}

/**
 * NotificationCount Domain Model 테스트
 */
class NotificationCountTest {

    @Test
    fun `hasUnread는 unreadCount가 0보다 클 때 true를 반환해야 함`() {
        // Given
        val count = NotificationCount(
            totalCount = 10,
            unreadCount = 3,
            readCount = 7,
        )

        // When & Then
        assertTrue(count.hasUnread)
    }

    @Test
    fun `hasUnread는 unreadCount가 0일 때 false를 반환해야 함`() {
        // Given
        val count = NotificationCount(
            totalCount = 10,
            unreadCount = 0,
            readCount = 10,
        )

        // When & Then
        assertFalse(count.hasUnread)
    }

    @Test
    fun `NotificationCount는 totalCount와 unreadCount, readCount의 합이 일치해야 함`() {
        // Given
        val totalCount = 10
        val unreadCount = 3
        val readCount = 7

        // When
        val count = NotificationCount(
            totalCount = totalCount,
            unreadCount = unreadCount,
            readCount = readCount,
        )

        // Then
        assertTrue(count.totalCount == count.unreadCount + count.readCount)
    }
}

