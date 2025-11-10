package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.invoice.InvoiceStatusEnum
import com.autoever.everp.domain.model.invoice.InvoiceTypeEnum
import com.autoever.everp.utils.serializer.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

/**
 * 재무관리(FCM, Financial and Cost Management) API Service
 * Base URL: /business/fcm
 */
interface FcmApi {

    // ========== 매입 인보이스 (AP - Accounts Payable) ==========
    @GET("$BASE_URL/invoice/ap")
    suspend fun getApInvoiceList(
        @Query("company") company: String? = null,
        @Query("startDate") startDate: LocalDate? = null,
        @Query("endDate") endDate: LocalDate? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<InvoiceListItemDto>>

    @GET("$BASE_URL/invoice/ap/{invoiceId}")
    suspend fun getApInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<InvoiceDetailResponseDto>

    /**
     * 공급사(Supplier)
     * 매입 전표 상태 수정
     * 확인 요청 -> 완납
     */
    @PATCH("$BASE_URL/invoice/ap/{invoiceId}")
    suspend fun updateApInvoice(
        @Path("invoiceId") invoiceId: String,
//        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

    /**
     * 매입 인보이스에 대한 수취 요청
     */
    @POST("$BASE_URL/invoice/ap/receivable/request")
    suspend fun requestReceivable(
        @Query("invoiceId") invoiceId: String,
    ): ApiResponse<Any>

    // ========== 매출 인보이스 (AR - Accounts Receivable) ==========
    @GET("$BASE_URL/invoice/ar")
    suspend fun getArInvoiceList(
        @Query("company") companyName: String? = null,
        @Query("startDate") startDate: LocalDate? = null,
        @Query("endDate") endDate: LocalDate? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<InvoiceListItemDto>>

    @GET("$BASE_URL/invoice/ar/{invoiceId}")
    suspend fun getArInvoiceDetail(
        @Path("invoiceId") invoiceId: String,
    ): ApiResponse<InvoiceDetailResponseDto>

    /**
     * 고객사(Customer)
     * 매출 전표 상태 수정
     * 미납 -> 확인 요청
     */
    @PATCH("$BASE_URL/invoice/ar/{invoiceId}")
    suspend fun updateArInvoice(
        @Path("invoiceId") invoiceId: String,
//        @Body request: InvoiceUpdateRequestDto,
    ): ApiResponse<Any>

//    @POST("$BASE_URL/invoice/ar/{invoiceId}/receivable/complete")
//    suspend fun completeReceivable(
//        @Path("invoiceId") invoiceId: String,
//    ): ApiResponse<Any>

    companion object {
        private const val BASE_URL = "business/fcm"
    }
}

@Serializable
data class InvoiceListItemDto(
    @SerialName("invoiceId")
    val invoiceId: String,
    @SerialName("invoiceNumber")
    val invoiceNumber: String,
    @SerialName("connection")
    val connection: InvoiceConnectionDto,
    @SerialName("totalAmount")
    val totalAmount: Double,
    @SerialName("issueDate")
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate,
    @SerialName("dueDate")
    @Serializable(with = LocalDateSerializer::class)
    val dueDate: LocalDate,
    @SerialName("statusCode")
    val statusCode: InvoiceStatusEnum, // PENDING, PAID, UNPAID
    @SerialName("reference")
    val reference: InvoiceReferenceDto,
)

@Serializable
data class InvoiceConnectionDto(
    @SerialName("companyId")
    val connectionId: String,
    @SerialName("companyCode")
    val connectionNumber: String,
    @SerialName("companyName")
    val connectionName: String,
)

@Serializable
data class InvoiceReferenceDto(
    @SerialName("referenceId")
    val referenceId: String,
    @SerialName("referenceNumber")
    val referenceNumber: String,
)

@Serializable
data class InvoiceDetailResponseDto(
    @SerialName("invoiceId")
    val invoiceId: String,
    @SerialName("invoiceNumber")
    val invoiceNumber: String,
    @SerialName("invoiceType")
    val invoiceType: InvoiceTypeEnum, // AP, AR
    @SerialName("statusCode")
    val statusCode: InvoiceStatusEnum, // PENDING, PAID, UNPAID
    @SerialName("issueDate")
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("name")
    val connectionName: String, // supplierName or customerName, 거래처 명
    @SerialName("referenceNumber")
    val referenceNumber: String,
    @SerialName("totalAmount")
    val totalAmount: Double,
    @SerialName("note")
    val note: String,
    @SerialName("items")
    val items: List<InvoiceDetailItemDto>,
)

@Serializable
data class InvoiceDetailItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("unitOfMaterialName")
    val unitOfMaterialName: String,
    @SerialName("unitPrice")
    val unitPrice: Double,
    @SerialName("totalPrice")
    val totalPrice: Double,
)

// TODO 전표 업데이트시 필요한지 확인 필요 - 기본값이 있는 경우 값이 전달되지 않음
@Serializable
data class InvoiceUpdateRequestDto(
    @SerialName("status")
    val status: InvoiceStatusEnum? = null, // PENDING, PAID, UNPAID
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate? = null,
    @SerialName("memo")
    val memo: String? = null,
)
