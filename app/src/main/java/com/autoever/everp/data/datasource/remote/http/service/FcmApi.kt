package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.*

/**
 * FCM(재무 관리) API Service
 * Base URL: /business/fcm
 */
interface FcmApi {

    // ========== 매입 인보이스 (AP - Accounts Payable) ==========
    @GET("$BASE_URL/invoice/ap")
    suspend fun getApInvoiceList(
        @Query("company") company: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<Any>

    @GET("$BASE_URL/invoice/ap/{invoiceId}")
    suspend fun getApInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    @PATCH("$BASE_URL/invoice/ap/{invoiceId}")
    suspend fun updateApInvoice(
        @Path("invoiceId") invoiceId: String,
        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

    @POST("$BASE_URL/invoice/ap/receivable/request")
    suspend fun requestReceivable(
        @Query("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    // ========== 매출 인보이스 (AR - Accounts Receivable) ==========
    @GET("$BASE_URL/invoice/ar")
    suspend fun getArInvoiceList(
        @Query("company") company: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<Any>

    @GET("$BASE_URL/invoice/ar/{invoiceId}")
    suspend fun getArInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    @PATCH("$BASE_URL/invoice/ar/{invoiceId}")
    suspend fun updateArInvoice(
        @Path("invoiceId") invoiceId: String,
        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

    @POST("$BASE_URL/invoice/ar/{invoiceId}/receivable/complete")
    suspend fun completeReceivable(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    companion object {
        private const val BASE_URL = "/business/fcm"
    }
}

@Serializable
data class FcmStatisticsDto(
    val data: Map<String, Any>,
)

@Serializable
data class InvoiceUpdateRequestDto(
    val status: String? = null,
    val dueDate: String? = null,
    val memo: String? = null,
)

