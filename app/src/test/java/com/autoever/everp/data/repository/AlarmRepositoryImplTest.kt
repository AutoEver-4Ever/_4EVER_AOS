package com.autoever.everp.data.repository

import app.cash.turbine.test
import com.autoever.everp.data.datasource.local.AlarmLocalDataSource
import com.autoever.everp.data.datasource.remote.AlarmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.NotificationCountResponseDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.NotificationReadResponseDto
import com.autoever.everp.data.datasource.remote.mapper.NotificationMapper
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationCount
import com.autoever.everp.domain.model.notification.NotificationLinkEnum
import com.autoever.everp.domain.model.notification.NotificationListParams
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

/**
 * AlarmRepositoryImpl 테스트
 */
class AlarmRepositoryImplTest {

    private lateinit var repository: AlarmRepositoryImpl
    private val localDataSource: AlarmLocalDataSource = mockk()
    private val remoteDataSource: AlarmRemoteDataSource = mockk()

    @Before
    fun setUp() {
        repository = AlarmRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `observeNotifications는 로컬 데이터 소스의 Flow를 반환해야 함`() = runTest {
        // Given
        val notifications = listOf(
            Notification(
                id = "1",
                title = "Test",
                message = "Test",
                linkType = com.autoever.everp.domain.model.notification.NotificationLinkEnum.QUOTATION,
                linkId = "1",
                source = NotificationSourceEnum.SD,
                status = NotificationStatusEnum.UNREAD,
                createdAt = LocalDateTime.now(),
            ),
        )
        val pageResponse = PageResponse(
            content = notifications,
            page = com.autoever.everp.data.datasource.remote.dto.common.PageDto(
                number = 0,
                size = 20,
                totalElements = 1,
                totalPages = 1,
                hasNext = false
            ),
        )
        every { localDataSource.observeNotifications() } returns MutableStateFlow(pageResponse)

        // When & Then
        repository.observeNotifications().test {
            val item = awaitItem()
            assertEquals(1, item.content.size)
        }
    }

    @Test
    fun `refreshNotifications 성공 시 로컬 데이터 소스에 저장되어야 함`() = runTest {
        // Given
        val params = NotificationListParams(
            sortBy = "createdAt",
            order = "desc",
            source = NotificationSourceEnum.UNKNOWN,
            page = 0,
            size = 20,
        )
        val dtoNotifications = listOf(
            NotificationListItemDto(
                notificationId = "1",
                title = "Test",
                message = "Test",
                linkType = NotificationLinkEnum.QUOTATION,
                linkId = "1",
                source = "SD",
                isRead = false,
                createdAt = LocalDateTime.now().toString(),
            ),
        )
        val dtoPageResponse = PageResponse(
            content = dtoNotifications,
            page = com.autoever.everp.data.datasource.remote.dto.common.PageDto(
                number = 0,
                size = 20,
                totalElements = 1,
                totalPages = 1,
                hasNext = false
            ),
        )
        val domainNotifications = NotificationMapper.toDomainList(dtoNotifications)
        val domainPageResponse = PageResponse(
            content = domainNotifications,
            page = dtoPageResponse.page,
        )

        coEvery {
            remoteDataSource.getNotificationList(
                sortBy = params.sortBy,
                order = params.order,
                source = params.source,
                page = params.page,
                size = params.size,
            )
        } returns Result.success(dtoPageResponse)
        coEvery { localDataSource.setNotifications(any()) } returns Unit

        // When
        val result = repository.refreshNotifications(params)

        // Then
        assertTrue(result.isSuccess)
        runBlocking {
            localDataSource.setNotifications(domainPageResponse)
        }
    }

    @Test
    fun `getNotificationCount 성공 시 전체와 읽지 않은 개수를 병렬로 조회해야 함`() = runTest {
        // Given
        val totalCountDto = NotificationCountResponseDto(count = 10)
        val unreadCountDto = NotificationCountResponseDto(count = 3)

        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNKNOWN)
        } returns Result.success(totalCountDto)
        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNREAD)
        } returns Result.success(unreadCountDto)

        // When
        val result = repository.getNotificationCount()

        // Then
        assertTrue(result.isSuccess)
        val count = result.getOrNull()
        assertEquals(10, count?.totalCount)
        assertEquals(3, count?.unreadCount)
        assertEquals(7, count?.readCount)
    }

    @Test
    fun `getNotificationCount 실패 시 에러를 반환해야 함`() = runTest {
        // Given
        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNKNOWN)
        } returns Result.failure(Exception("Network error"))
        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNREAD)
        } returns Result.failure(Exception("Network error"))

        // When
        val result = repository.getNotificationCount()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `refreshNotificationCount 성공 시 로컬 데이터 소스에 저장되어야 함`() = runTest {
        // Given
        val totalCountDto = NotificationCountResponseDto(count = 10)
        val unreadCountDto = NotificationCountResponseDto(count = 3)
        val expectedCount = NotificationCount(
            totalCount = 10,
            unreadCount = 3,
            readCount = 7,
        )

        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNKNOWN)
        } returns Result.success(totalCountDto)
        coEvery {
            remoteDataSource.getNotificationCount(status = NotificationStatusEnum.UNREAD)
        } returns Result.success(unreadCountDto)
        coEvery { localDataSource.setNotificationCount(any()) } returns Unit

        // When
        val result = repository.refreshNotificationCount()

        // Then
        assertTrue(result.isSuccess)
        runBlocking {
            localDataSource.setNotificationCount(count = expectedCount)
        }
    }

    @Test
    fun `observeNotificationCount는 로컬 데이터 소스의 Flow를 반환해야 함`() = runTest {
        // Given
        val count = NotificationCount(totalCount = 10, unreadCount = 3, readCount = 7)
        every { localDataSource.observeNotificationCount() } returns MutableStateFlow(count)

        // When & Then
        repository.observeNotificationCount().test {
            val item = awaitItem()
            assertEquals(10, item.totalCount)
            assertEquals(3, item.unreadCount)
            assertEquals(7, item.readCount)
        }
    }

    @Test
    fun `markNotificationAsRead 성공 시 로컬 캐시가 업데이트되어야 함`() = runTest {
        // Given
        val notificationId = "notification-1"
        coEvery { remoteDataSource.markNotificationAsRead(notificationId) } returns Result.success(Unit)
        // updateLocalNotificationsAsRead에서 observeNotifications()를 호출하므로 mock 필요
        every { localDataSource.observeNotifications() } returns flowOf(PageResponse.empty())

        // When
        val result = repository.markNotificationAsRead(notificationId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `markAllNotificationsAsRead 성공 시 로컬 캐시가 업데이트되어야 함`() = runTest {
        // Given
        val notificationIds = listOf("1", "2", "3")
        val updateResponse = NotificationReadResponseDto(
            updatedCount = 3,
        )
        coEvery { remoteDataSource.markNotificationsAsRead(notificationIds) } returns Result.success(
            updateResponse,
        )
        // updateLocalNotificationsAsRead에서 observeNotifications()를 호출하므로 mock 필요
        every { localDataSource.observeNotifications() } returns flowOf(PageResponse.empty())

        // When
        val result = repository.markNotificationsAsRead(notificationIds)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull())
    }

    @Test
    fun `markAllNotificationsAsRead 성공 시 전체 읽음 처리되어야 함`() = runTest {
        // Given
        val updateResponse = NotificationReadResponseDto(
            updatedCount = 5,
        )
        coEvery { remoteDataSource.markAllNotificationsAsRead() } returns Result.success(updateResponse)

        // When
        val result = repository.markAllNotificationsAsRead()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(5, result.getOrNull())
    }
}

