package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.customer.CustomerStatusEnum
import com.autoever.everp.domain.model.quotation.QuotationSearchTypeEnum
import com.autoever.everp.domain.model.quotation.QuotationStatusEnum
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
import com.autoever.everp.domain.model.sale.SalesOrderStatusEnum
import com.autoever.everp.utils.serializer.LocalDateSerializer
import kotlinx.serialization.Required
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
 * 영업관리(SD, Sales & Distribution) 관련 API Service
 * Base URL: /business/sd
 */
interface SdApi {
    // ========== 견적서 관리 ==========

    /**
     * 견적 목록 조회 (페이지네이션)
     */
    @GET("$BASE_URL/quotations")
    suspend fun getQuotationList(
        @Query("startDate") startDate: LocalDate? = null,
        @Query("endDate") endDate: LocalDate? = null,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null, // BUSINESS 서버의 Quotation Entity의 필드 값 기준
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<QuotationListItemDto>>

    /**
     * 견적 상세 조회
     */
    @GET("$BASE_URL/quotations/{quotationId}")
    suspend fun getQuotationDetail(
        @Path("quotationId") quotationId: String,
    ): ApiResponse<QuotationDetailResponseDto>

    /**
     * 신규 견적서 생성
     */
    @POST("$BASE_URL/quotations")
    suspend fun createQuotation(
        @Body request: QuotationCreateRequestDto,
    ): ApiResponse<QuotationCreateResponseDto>

    // ========== 고객사 관리 ==========

    /**
     * 고객사 상세 조회
     */
    @GET("$BASE_URL/customers/{customerId}")
    suspend fun getCustomerDetail(
        @Path("customerId") customerId: String,
    ): ApiResponse<CustomerDetailResponseDto>

    /**
     * 고객사 정보 수정
     */
    @PATCH("$BASE_URL/customers/{customerId}")
    suspend fun updateCustomer(
        @Path("customerId") customerId: String,
        @Body request: CustomerUpdateRequestDto,
    ): ApiResponse<Any>

    // ========== 주문서 관리 ==========

    /**
     * 주문서 목록 조회
     */
    @GET("$BASE_URL/orders")
    suspend fun getSalesOrderList(
        @Query("startDate") startDate: LocalDate? = null,
        @Query("endDate") endDate: LocalDate? = null,
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<PageResponse<SalesOrderListItemDto>>

    /**
     * 주문서 상세 조회
     */
    @GET("$BASE_URL/orders/{salesOrderId}")
    suspend fun getSalesOrderDetail(
        @Path("salesOrderId") salesOrderId: String,
    ): ApiResponse<SalesOrderDetailResponseDto>

    companion object {
        const val BASE_URL = "business/sd"
    }
}

// ========== DTOs ==========
// 견적서
@Serializable
data class QuotationListItemDto(
    @SerialName("quotationId")
    val quotationId: Long,
    @SerialName("quotationNumber")
    val quotationNumber: String,
    @SerialName("customerName")
    val customerName: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("statusCode")
    val statusCode: QuotationStatusEnum = QuotationStatusEnum.UNKNOWN,
    @SerialName("productId")
    val productId: String, // 첫번째 상품 ID
    @SerialName("quantity")
    val quantity: Int, // 첫번째 상품 수량
    @SerialName("uomName")
    val uomName: String, // 첫번째 상품 단위 이름
)

@Serializable
data class QuotationDetailResponseDto(
    @SerialName("quotationId")
    val quotationId: String,
    @SerialName("quotationNumber")
    val quotationNumber: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("quotationDate")
    val quotationDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("statusCode")
    val statusCode: QuotationStatusEnum,
    @SerialName("customerName")
    val customerName: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("items")
    val items: List<QuotationItemDto>,
    @SerialName("totalAmount")
    val totalAmount: Long,
)

@Serializable
data class QuotationItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("uomName")
    val uomName: String,
    @SerialName("unitPrice")
    val unitPrice: Long,
    @SerialName("totalPrice")
    val totalPrice: Long,
)

@Serializable
data class QuotationCreateRequestDto(
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("items")
    val items: List<QuotationCreateRequestItemDto>,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class QuotationCreateRequestItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("unitPrice")
    val unitPrice: Long,
)

@Serializable
data class QuotationCreateResponseDto(
    @SerialName("quotationId")
    val quotationId: String,
)

// 고객사

@Serializable
data class CustomerDetailResponseDto(
    @SerialName("customerId")
    val customerId: String,
    @SerialName("customerNumber")
    val customerNumber: String,
    @SerialName("customerName")
    val customerName: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("statusCode")
    val customerStatusCode: CustomerStatusEnum,
    @SerialName("customerPhone")
    val contactPhone: String,
    @SerialName("customerEmail")
    val contactEmail: String,
    // 기본 주소
    @SerialName("baseAddress")
    val address: String,
    // 상세 주소
    @SerialName("detailAddress")
    val detailAddress: String? = null,
    @SerialName("manager")
    val manager: CustomerManagerDto,
    // 총 주문 수
    @SerialName("totalOrders")
    val totalOrders: Long,
    // 총 거래 금액
    @SerialName("totalTransactionAmount")
    val totalTransactionAmount: Long,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class CustomerManagerDto(
    @SerialName("managerName")
    val managerName: String,
    @SerialName("managerPhone")
    val managerPhone: String,
    @SerialName("managerEmail")
    val managerEmail: String,
)

@Serializable
data class CustomerUpdateRequestDto(
    @SerialName("customerName")
    val customerName: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("customerPhone")
    val customerPhone: String,
    @SerialName("customerEmail")
    val customerEmail: String,
    @SerialName("baseAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detailAddress: String,
    @Required
    @SerialName("statusCode")
    val statusCode: CustomerStatusEnum = CustomerStatusEnum.ACTIVE,
    @SerialName("manager")
    val manager: CustomerManagerDto,
    @SerialName("note")
    val note: String? = null,
)

// 주문서
@Serializable
data class SalesOrderListItemDto(
    @SerialName("salesOrderId")
    val salesOrderId: String,
    @SerialName("salesOrderNumber")
    val salesOrderNumber: String,
    @SerialName("customerName")
    val customerName: String,
    @SerialName("manager")
    val customerManager: CustomerManagerDto,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("orderDate")
    val orderDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("totalAmount")
    val totalAmount: Long,
    @SerialName("statusCode")
    val statusCode: SalesOrderStatusEnum,
)

@Serializable
data class SalesOrderDetailResponseDto(
    @SerialName("order")
    val order: SalesOrderDetailDto,
    @SerialName("customer")
    val customer: SalesOrderCustomerDto,
    @SerialName("items")
    val items: List<SalesOrderItemDto>,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class SalesOrderCustomerDto(
    @SerialName("customerId")
    val customerId: String,
    @SerialName("customerName")
    val customerName: String,
    @SerialName("baseAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detailAddress: String,
    @SerialName("manager")
    val manager: CustomerManagerDto,
)

@Serializable
data class SalesOrderDetailDto(
    @SerialName("salesOrderId")
    val salesOrderId: String,
    @SerialName("salesOrderNumber")
    val salesOrderNumber: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("orderDate")
    val orderDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("statusCode")
    val statusCode: SalesOrderStatusEnum,
    @SerialName("totalAmount")
    val totalAmount: Long,
)

@Serializable
data class SalesOrderItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("unitPrice")
    val unitPrice: Long,
    @SerialName("totalPrice")
    val totalPrice: Long,
)

