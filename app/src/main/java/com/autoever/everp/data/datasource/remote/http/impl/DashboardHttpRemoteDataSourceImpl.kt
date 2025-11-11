package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.DashboardRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.DashboardApi
import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.user.UserRoleEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardHttpRemoteDataSourceImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
) : DashboardRemoteDataSource {

    override suspend fun getDashboardWorkflows(
        role: UserRoleEnum,
    ): Result<DashboardWorkflowsResponseDto> = withContext(Dispatchers.IO) {
        runCatching {
            val response = dashboardApi.getDashboardWorkflows(role)
            response.data ?: throw NoSuchElementException("Dashboard workflows data is null for role: $role")
        }
    }

}
