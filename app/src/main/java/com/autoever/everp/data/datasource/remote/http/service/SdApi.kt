package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.dto.response.QuotationListItem
import retrofit2.http.GET
import retrofit2.http.Query

interface SdApi {
    @GET("$BASE_URL/quotations")
    suspend fun fetchQuotationPageableList(
        @Query("status") status: String, // e.g., "APPROVED", "PENDING"
        @Query("startDate") startDate: String? = null, // e.g., "2024-01-01"
        @Query("endDate") endDate: String? = null, // e.g., "2024-12-31"
        @Query("search") searchKeyword: String = "ALL",
        @Query("sort") sortBy: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<QuotationListItem>>

    companion object {
        const val BASE_URL = "/business/sd"
    }
}
