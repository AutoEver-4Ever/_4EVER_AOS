package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.QuotationItemDto
import com.autoever.everp.data.datasource.remote.dto.response.PagenatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SdApiService {
    @GET("$SD_SERVICE_URL/quotations")
    suspend fun fetchQuotationPageableList(
        @Query("status") status: String, // e.g., "APPROVED", "PENDING"
        @Query("startDate") startDate: String? = null, // e.g., "2024-01-01"
        @Query("endDate") endDate: String? = null, // e.g., "2024-12-31"
        @Query("search") searchKeyword: String? = null,
        @Query("sort") sortBy: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): PagenatedResponseDto<QuotationItemDto>

    companion object {
        const val SD_SERVICE_URL = "api/business/sd"
    }
}
