package com.autoever.everp.data.datasource.remote.http.service


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
    ): DashboardWorkflowsResponseDto

    companion object {
        private const val BASE_URL = "dashboard"
    }
}

@Serializable
data class DashboardWorkflowsResponseDto(
    @SerialName("role")
    val role: String,
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
            val workflowId: String,
            @SerialName("itemNumber")
            val count: Int,
            @SerialName("itemTitle")
            val workflowName: String,
            @SerialName("name")
            val name: String,
            @SerialName("statusCode")
            val statusCode: String,
            @SerialName("data")
            @Serializable(with = LocalDateSerializer::class)
            val data: LocalDate,
        )
    }
}


/*

@GET("$BASE_URL/statistics")
    suspend fun getDashboardStatistics(): DashboardStatisticsResponseDto
      
 */
