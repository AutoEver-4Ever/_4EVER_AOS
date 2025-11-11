package com.autoever.everp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.dashboard.DashboardTapEnum
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import com.autoever.everp.domain.model.notification.NotificationStatusEnum
import com.autoever.everp.domain.repository.AlarmRepository
import com.autoever.everp.domain.repository.DashboardRepository
import com.autoever.everp.domain.repository.UserRepository
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
class SupplierHomeViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val userRepository: UserRepository,
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val _recentActivities = MutableStateFlow<List<DashboardWorkflows.DashboardWorkflowTab>>(emptyList())
    val recentActivities: StateFlow<List<DashboardWorkflows.DashboardWorkflowTab>>
        get() = _recentActivities.asStateFlow()

    private val _categoryMap = MutableStateFlow<Map<String, DashboardTapEnum>>(emptyMap())
    val categoryMap: StateFlow<Map<String, DashboardTapEnum>>
        get() = _categoryMap.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasUnreadNotifications = MutableStateFlow(false)
    val hasUnreadNotifications: StateFlow<Boolean> = _hasUnreadNotifications.asStateFlow()

    init {
        loadRecentActivities()
        observeNotificationCount()
        refreshNotificationCount()
    }

    fun loadRecentActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.getUserInfo().onSuccess { userInfo ->
                    val role = userInfo.userRole
                    dashboardRepository.refreshWorkflows(role).onSuccess {
                        dashboardRepository.getWorkflows(role).onSuccess { workflows ->
                            // tabs를 날짜순으로 정렬
                            val sortedTabs = workflows.tabs.sortedByDescending { it.createdAt }
                                .take(5) // 최근 5개만

                            _recentActivities.value = sortedTabs
                            _categoryMap.value = sortedTabs.associate { it.id to it.tabCode }
                        }.onFailure { e ->
                            Timber.e(e, "워크플로우 조회 실패")
                        }
                    }.onFailure { e ->
                        Timber.e(e, "워크플로우 갱신 실패")
                    }
                }.onFailure { e ->
                    Timber.e(e, "사용자 정보 조회 실패")
                }
            } catch (e: Exception) {
                Timber.e(e, "최근 활동 로드 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadRecentActivities()
        refreshNotificationCount()
    }

    private fun observeNotificationCount() {
        alarmRepository.observeNotificationCount()
            .onEach { count ->
                _hasUnreadNotifications.value = count.unreadCount >= 1
            }
            .launchIn(viewModelScope)
    }

    private fun refreshNotificationCount() {
        viewModelScope.launch {
            alarmRepository.refreshNotificationCount()
                .onFailure { e ->
                    Timber.e(e, "알림 개수 갱신 실패")
                }
        }
    }
}
