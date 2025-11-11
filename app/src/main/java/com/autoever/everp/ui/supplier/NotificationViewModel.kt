package com.autoever.everp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationListParams
import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import com.autoever.everp.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val _notifications = MutableStateFlow<PageResponse<Notification>>(PageResponse.empty())
    val notifications: StateFlow<PageResponse<Notification>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeNotifications()
        loadNotifications()
    }

    private fun observeNotifications() {
        alarmRepository.observeNotifications()
            .onEach { page ->
                _notifications.value = page
            }
            .launchIn(viewModelScope)
    }

    fun loadNotifications(
        sortBy: String = "createdAt",
        order: String = "desc",
        source: NotificationSourceEnum = NotificationSourceEnum.UNKNOWN,
        page: Int = 0,
        size: Int = 20,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val params = NotificationListParams(
                    sortBy = sortBy,
                    order = order,
                    source = source,
                    page = page,
                    size = size,
                )
                alarmRepository.refreshNotifications(params)
                    .onFailure { e ->
                        Timber.e(e, "알림 목록 로드 실패")
                        _error.value = "알림 목록을 불러오는데 실패했습니다."
                    }
            } catch (e: Exception) {
                Timber.e(e, "알림 목록 로드 중 예외 발생")
                _error.value = "알림 목록을 불러오는데 실패했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            alarmRepository.markNotificationAsRead(notificationId)
                .onFailure { e ->
                    Timber.e(e, "알림 읽음 처리 실패")
                }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            alarmRepository.markAllNotificationsAsRead()
                .onSuccess {
                    // 성공 시 알림 목록 다시 로드
                    loadNotifications()
                }
                .onFailure { e ->
                    Timber.e(e, "전체 알림 읽음 처리 실패")
                }
        }
    }

    fun refresh() {
        loadNotifications()
    }
}

