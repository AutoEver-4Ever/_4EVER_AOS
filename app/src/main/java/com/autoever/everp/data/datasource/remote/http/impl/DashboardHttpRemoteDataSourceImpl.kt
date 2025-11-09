package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.DashboardRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.DashboardApi
import com.autoever.everp.data.datasource.remote.http.service.DashboardWorkflowsResponseDto
import com.autoever.everp.domain.model.user.UserRoleEnum
import javax.inject.Inject

class DashboardHttpRemoteDataSourceImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
) : DashboardRemoteDataSource {

    override suspend fun getDashboardWorkflows(role: UserRoleEnum): Result<DashboardWorkflowsResponseDto> =
        runCatching {
            dashboardApi.getDashboardWorkflows(role)
        }
}

