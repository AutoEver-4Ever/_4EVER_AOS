package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.DashboardLocalDataSource
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardLocalDataSourceImpl @Inject constructor() : DashboardLocalDataSource {
    private val workflowsFlow = MutableStateFlow<DashboardWorkflows?>(null)

    override fun observeWorkflows(): Flow<DashboardWorkflows?> = workflowsFlow.asStateFlow()

    override suspend fun setWorkflows(workflows: DashboardWorkflows) {
        workflowsFlow.value = workflows
    }

    override suspend fun clear() {
        workflowsFlow.value = null
    }
}

