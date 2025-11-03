package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import retrofit2.http.*

/**
 * FCM(재무 관리) API Service
 * Base URL: /business/fcm
 */
interface FcmFinanceApiService {

    // ========== 통계 ==========
    @GET("business/fcm/statictics")
    suspend fun getStatistics(
        @Query("periods") periods: String? = null,
    ): ApiResponse<FcmStatisticsDto>

    // ========== 매입 인보이스 (AP - Accounts Payable) ==========
    @GET("business/fcm/invoice/ap")
    suspend fun getApInvoiceList(
        @Query("company") company: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<Any>

    @GET("business/fcm/invoice/ap/{invoiceId}")
    suspend fun getApInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    @PATCH("business/fcm/invoice/ap/{invoiceId}")
    suspend fun updateApInvoice(
        @Path("invoiceId") invoiceId: String,
        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

    @POST("business/fcm/invoice/ap/receivable/request")
    suspend fun requestReceivable(
        @Query("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    // ========== 매출 인보이스 (AR - Accounts Receivable) ==========
    @GET("business/fcm/invoice/ar")
    suspend fun getArInvoiceList(
        @Query("company") company: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<Any>

    @GET("business/fcm/invoice/ar/{invoiceId}")
    suspend fun getArInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    @PATCH("business/fcm/invoice/ar/{invoiceId}")
    suspend fun updateArInvoice(
        @Path("invoiceId") invoiceId: String,
        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

    @POST("business/fcm/invoice/ar/{invoiceId}/receivable/complete")
    suspend fun completeReceivable(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>
}

@kotlinx.serialization.Serializable
data class FcmStatisticsDto(
    val data: Map<String, Any>,
)

@kotlinx.serialization.Serializable
data class InvoiceUpdateRequestDto(
    val status: String? = null,
    val dueDate: String? = null,
    val memo: String? = null,
)

