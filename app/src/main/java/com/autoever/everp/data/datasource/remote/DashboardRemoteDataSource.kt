package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.user.UserRoleEnum

interface DashboardRemoteDataSource {
    suspend fun getDashboardWorkflows(role: UserRoleEnum): Result<DashboardWorkflowsResponseDto>
}

