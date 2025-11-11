package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import com.autoever.everp.domain.model.user.UserRoleEnum
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeWorkflows(): Flow<DashboardWorkflows?>
    suspend fun refreshWorkflows(role: UserRoleEnum): Result<Unit>
    suspend fun getWorkflows(role: UserRoleEnum): Result<DashboardWorkflows>
}

