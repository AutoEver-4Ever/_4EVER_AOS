package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.DashboardLocalDataSource
import com.autoever.everp.data.datasource.remote.DashboardRemoteDataSource
import com.autoever.everp.data.datasource.remote.mapper.DashboardMapper
import com.autoever.everp.domain.model.dashboard.DashboardWorkflows
import com.autoever.everp.domain.model.user.UserRoleEnum
import com.autoever.everp.domain.repository.DashboardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardLocalDataSource: DashboardLocalDataSource,
    private val dashboardRemoteDataSource: DashboardRemoteDataSource,
) : DashboardRepository {

    override fun observeWorkflows(): Flow<DashboardWorkflows?> =
        dashboardLocalDataSource.observeWorkflows()

    override suspend fun refreshWorkflows(role: UserRoleEnum): Result<Unit> = withContext(Dispatchers.Default) {
        getWorkflows(role).map { workflows ->
            dashboardLocalDataSource.setWorkflows(workflows)
        }
    }

    override suspend fun getWorkflows(role: UserRoleEnum): Result<DashboardWorkflows> = withContext(Dispatchers.Default) {
        dashboardRemoteDataSource.getDashboardWorkflows(role)
            .map { DashboardMapper.toDomain(it) }
    }
}

