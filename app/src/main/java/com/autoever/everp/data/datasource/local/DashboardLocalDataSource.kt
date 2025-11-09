package com.autoever.everp.data.datasource.local

import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import kotlinx.coroutines.flow.Flow

interface DashboardLocalDataSource {
    fun observeWorkflows(): Flow<DashboardWorkflows?>
    suspend fun setWorkflows(workflows: DashboardWorkflows)
    suspend fun clear()
}

