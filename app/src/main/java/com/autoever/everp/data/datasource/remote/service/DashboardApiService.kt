package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 대시보드 API Service
 * Base URL: /dashboard
 */
interface DashboardApiService {

    /**
     * 워크플로우 조회
     * @param role 형식: <MODULE>_(USER|ADMIN)
     * MODULE: SD, MM, IM, PP, HRM, FIN
     * 예: SD_USER, MM_ADMIN
     */
    @GET("$BASE_URL/workflows")
    suspend fun getWorkflows(
        @Query("role") role: String,
    ): ApiResponse<DashboardWorkflowResponseDto>

    /**
     * 대시보드 통계 조회
     */
    @GET("$BASE_URL/statistics")
    suspend fun getStatistics(): ApiResponse<DashboardStatisticsResponseDto>

    companion object {
        private const val BASE_URL = "/dashboard"
    }
}

@kotlinx.serialization.Serializable
data class DashboardWorkflowResponseDto(
    val workflows: List<WorkflowItemDto>,
)

@kotlinx.serialization.Serializable
data class WorkflowItemDto(
    val workflowId: String,
    val title: String,
    val description: String?,
    val status: String,
    val count: Int,
)

@kotlinx.serialization.Serializable
data class DashboardStatisticsResponseDto(
    val statistics: Map<String, Any>,
)

