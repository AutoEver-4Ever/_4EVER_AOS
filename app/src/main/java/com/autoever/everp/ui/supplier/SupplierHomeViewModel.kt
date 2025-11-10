package com.autoever.everp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.dashboard.DashboardTapEnum
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import com.autoever.everp.domain.repository.DashboardRepository
import com.autoever.everp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SupplierHomeViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _recentActivities = MutableStateFlow<List<DashboardWorkflows.DashboardWorkflowItem>>(emptyList())
    val recentActivities: StateFlow<List<DashboardWorkflows.DashboardWorkflowItem>>
        get() = _recentActivities.asStateFlow()

    private val _categoryMap = MutableStateFlow<Map<String, DashboardTapEnum>>(emptyMap())
    val categoryMap: StateFlow<Map<String, DashboardTapEnum>>
        get() = _categoryMap.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRecentActivities()
    }

    fun loadRecentActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.getUserInfo().onSuccess { userInfo ->
                    val role = userInfo.userRole
                    dashboardRepository.refreshWorkflows(role).onSuccess {
                        dashboardRepository.getWorkflows(role).onSuccess { workflows ->
                            // 모든 tabs의 items를 하나의 리스트로 합치고 날짜순으로 정렬
                            val allItems = workflows.tabs.flatMap { tab ->
                                tab.items.map { item ->
                                    item to tab.tabCode
                                }
                            }.sortedByDescending { it.first.createdAt }
                                .take(10) // 최근 10개만

                            _recentActivities.value = allItems.map { it.first }
                            _categoryMap.value = allItems.associate { it.first.id to it.second }
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
    }
}
