package com.autoever.everp.data.datasource.remote.http.service


import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.domain.model.user.UserRoleEnum
import com.autoever.everp.utils.serializer.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface DashboardApi {

    /**
     * 대시보드 워크플로우 조회
     */
    @GET("$BASE_URL/workflows")
    suspend fun getDashboardWorkflows(
        @Query("role") role: UserRoleEnum,
    ): ApiResponse<DashboardWorkflowsResponseDto>

    companion object {
        private const val BASE_URL = "dashboard"
    }
}

@Serializable
data class DashboardWorkflowsResponseDto(
    @SerialName("tabs")
    val tabs: List<DashboardWorkflowTabDto>,
) {
    @Serializable
    data class DashboardWorkflowTabDto(
        @SerialName("tabCode")
        val tabCode: String,
        @SerialName("items")
        val items: List<DashboardWorkflowTabItemDto>,
    ) {
        @Serializable
        data class DashboardWorkflowTabItemDto(
            @SerialName("itemId")
            val itemId: String,
            @SerialName("itemNumber")
            val itemNumber: String,
            @SerialName("itemTitle")
            val itemTitle: String, // workflow name
            @SerialName("name")
            val name: String, // 고객명 or 공급사명
            @SerialName("statusCode")
            val statusCode: String,
            @SerialName("date")
            @Serializable(with = LocalDateSerializer::class)
            val date: LocalDate,
        )
    }
}


/*

@GET("$BASE_URL/statistics")
    suspend fun getDashboardStatistics(): DashboardStatisticsResponseDto

 */
