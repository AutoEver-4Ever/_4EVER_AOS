package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.purchase.PurchaseOrderSearchTypeEnum
import com.autoever.everp.domain.model.purchase.PurchaseOrderStatusEnum
import com.autoever.everp.utils.serializer.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*
import java.time.LocalDate

/**
 * 자재 관리(MM, Materials Management) API Service
 * Base URL: /scm-pp/mm
 */
interface MmApi {

    // ========== 공급업체 관리 ==========

    /**
     * 공급업체 상세 조회
     */
    @GET("$BASE_URL/supplier/{supplierId}")
    suspend fun getSupplierDetail(
        @Path("supplierId") supplierId: String,
    ): ApiResponse<SupplierDetailResponseDto>

    /**
     * 공급업체 수정
     */
    @PATCH("$BASE_URL/supplier/{supplierId}")
    suspend fun updateSupplier(
        @Path("supplierId") supplierId: String,
        @Body request: SupplierUpdateRequestDto,
    ): ApiResponse<Any>

    // ========== 발주서 관리 ==========

    /**
     * 발주서 목록 조회
     * @param statusCode 상태 코드 (ALL, APPROVAL, PENDING, REJECTED, DELIVERING, DELIVERED)
     * @param type 검색 타입 (SupplierCompanyName, PurchaseOrderNumber)
     * @param keyword 검색 키워드
     * @param startDate 시작일
     * @param endDate 종료일
     * @param page 페이지 번호
     * @param size 페이지 크기
     */
    @GET("$BASE_URL/purchase-orders")
    suspend fun getPurchaseOrderList(
        @Query("statusCode") statusCode: String = "ALL",
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("startDate") startDate: LocalDate? = null,
        @Query("endDate") endDate: LocalDate? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
    ): ApiResponse<PageResponse<PurchaseOrderListItemDto>>

    /**
     * 발주서 상세 조회
     */
    @GET("$BASE_URL/purchase-orders/{purchaseOrderId}")
    suspend fun getPurchaseOrderDetail(
        @Path("purchaseOrderId") purchaseOrderId: String,
    ): ApiResponse<PurchaseOrderDetailResponseDto>

    /**
     * 발주서 승인
     */
    @POST("$BASE_URL/purchase-orders/{purchaseOrderId}/approve")
    suspend fun approvePurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Query("requesterId") requesterId: String,
    ): ApiResponse<Any>

    /**
     * 발주서 반려
     */
    @POST("$BASE_URL/purchase-orders/{purchaseOrderId}/reject")
    suspend fun rejectPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Query("requesterId") requesterId: String,
        @Body request: PurchaseOrderRejectRequestDto,
    ): ApiResponse<Any>

    // ========== 통계 및 토글 ==========

    /**
     * MM 통계 조회
     */
    @GET("$BASE_URL/statistics")
    suspend fun getMMStatistics(): ApiResponse<MmStatisticsResponseDto>

    /**
     * 구매요청서 상태 토글
     */
    @GET("$BASE_URL/purchase-requisition/status/toggle")
    suspend fun getPurchaseRequisitionStatusToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 발주서 상태 토글
     */
    @GET("$BASE_URL/purchase-orders/status/toggle")
    suspend fun getPurchaseOrderStatusToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 공급업체 상태 토글
     */
    @GET("$BASE_URL/supplier/status/toggle")
    suspend fun getSupplierStatusToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 공급업체 카테고리 토글
     */
    @GET("$BASE_URL/supplier/category/toggle")
    suspend fun getSupplierCategoryToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 구매 요청 검색 타입 토글
     */
    @GET("$BASE_URL/purchase-requisition/search-type/toggle")
    suspend fun getPurchaseRequisitionSearchTypeToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 발주서 검색 타입 토글
     */
    @GET("$BASE_URL/purchase-orders/search-type/toggle")
    suspend fun getPurchaseOrderSearchTypeToggle(): ApiResponse<List<ToggleItemDto>>

    /**
     * 공급업체 검색 타입 토글
     */
    @GET("$BASE_URL/supplier/search-type/toggle")
    suspend fun getSupplierSearchTypeToggle(): ApiResponse<List<ToggleItemDto>>

    companion object {
        private const val BASE_URL = "/scm-pp/mm"
    }
}

// ========== DTOs ==========

// 공급업체
@Serializable
data class SupplierListItemDto(
    @SerialName("supplierId")
    val supplierId: String,
    @SerialName("supplierNumber")
    val supplierNumber: String,
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("category")
    val category: String,
    @SerialName("managerName")
    val managerName: String,
    @SerialName("managerPhone")
    val managerPhone: String,
    @SerialName("statusCode")
    val statusCode: String,
)

@Serializable
data class SupplierDetailResponseDto(
    @SerialName("supplierId")
    val supplierId: String,
    @SerialName("supplierNumber")
    val supplierNumber: String,
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("category")
    val category: String,
    @SerialName("contactPhone")
    val contactPhone: String,
    @SerialName("contactEmail")
    val contactEmail: String,
    @SerialName("zipCode")
    val zipCode: String,
    @SerialName("address")
    val address: String,
    @SerialName("detailAddress")
    val detailAddress: String? = null,
    @SerialName("manager")
    val manager: SupplierManagerDto,
    @SerialName("statusCode")
    val statusCode: String,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class SupplierManagerDto(
    @SerialName("name")
    val name: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("email")
    val email: String,
)

@Serializable
data class SupplierCreateRequestDto(
    @SerialName("companyName")
    val companyName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("category")
    val category: String,
    @SerialName("contactPhone")
    val contactPhone: String,
    @SerialName("contactEmail")
    val contactEmail: String,
    @SerialName("zipCode")
    val zipCode: String,
    @SerialName("address")
    val address: String,
    @SerialName("detailAddress")
    val detailAddress: String? = null,
    @SerialName("manager")
    val manager: SupplierManagerDto,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class SupplierCreateResponseDto(
    @SerialName("supplierId")
    val supplierId: String,
    @SerialName("supplierNumber")
    val supplierNumber: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("username")
    val username: String,
)

@Serializable
data class SupplierUpdateRequestDto(
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("category")
    val category: String,
    @SerialName("supplierPhone")
    val supplierPhone: String,
    @SerialName("supplierEmail")
    val supplierEmail: String,
    @SerialName("baseAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detailAddress: String? = null,
    @SerialName("statusCode")
    val statusCode: String,
    @SerialName("manager")
    val manager: SupplierManagerDto,
    @SerialName("note")
    val note: String? = null,
)

// 구매요청서
@Serializable
data class StockPurchaseRequestDto(
    @SerialName("items")
    val items: List<StockPurchaseItemDto>,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class StockPurchaseItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("quantity")
    val quantity: Int,
)

@Serializable
data class PurchaseRequisitionListItemDto(
    @SerialName("purchaseRequisitionId")
    val purchaseRequisitionId: String,
    @SerialName("productRequestNumber")
    val productRequestNumber: String,
    @SerialName("requesterName")
    val requesterName: String,
    @SerialName("departmentName")
    val departmentName: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("requestDate")
    val requestDate: LocalDate,
    @SerialName("itemsSummary")
    val itemsSummary: String,
    @SerialName("statusCode")
    val statusCode: String,
)

@Serializable
data class PurchaseRequisitionDetailResponseDto(
    @SerialName("purchaseRequisitionId")
    val purchaseRequisitionId: String,
    @SerialName("productRequestNumber")
    val productRequestNumber: String,
    @SerialName("statusCode")
    val statusCode: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("requestDate")
    val requestDate: LocalDate,
    @SerialName("requesterId")
    val requesterId: String,
    @SerialName("requesterName")
    val requesterName: String,
    @SerialName("departmentName")
    val departmentName: String,
    @SerialName("items")
    val items: List<PurchaseRequisitionItemDto>,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class PurchaseRequisitionItemDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("uomName")
    val uomName: String,
)

@Serializable
data class PurchaseRequisitionCreateRequestDto(
    @SerialName("items")
    val items: List<PurchaseRequisitionItemRequestDto>,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class PurchaseRequisitionItemRequestDto(
    @SerialName("itemId")
    val itemId: String,
    @SerialName("quantity")
    val quantity: Int,
)

@Serializable
data class PurchaseRequisitionRejectRequestDto(
    @SerialName("reason")
    val reason: String,
)

// 발주서
@Serializable
data class PurchaseOrderListItemDto(
    @SerialName("purchaseOrderId")
    val purchaseOrderId: String,
    @SerialName("purchaseOrderNumber")
    val purchaseOrderNumber: String,
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("itemsSummary")
    val itemsSummary: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("orderDate")
    val orderDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("totalAmount")
    val totalAmount: Long,
    @SerialName("statusCode")
    val statusCode: String,
)

@Serializable
data class PurchaseOrderDetailResponseDto(
    @SerialName("purchaseOrderId")
    val purchaseOrderId: String,
    @SerialName("purchaseOrderNumber")
    val purchaseOrderNumber: String,
    @SerialName("statusCode")
    val statusCode: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("orderDate")
    val orderDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("supplierId")
    val supplierId: String,
    @SerialName("supplierNumber")
    val supplierNumber: String,
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("managerPhone")
    val managerPhone: String,
    @SerialName("managerEmail")
    val managerEmail: String,
    @SerialName("items")
    val items: List<PurchaseOrderDetailItemDto>,
    @SerialName("totalAmount")
    val totalAmount: Long,
    @SerialName("note")
    val note: String? = null,
)

@Serializable
data class PurchaseOrderDetailItemDto(
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
data class PurchaseOrderRejectRequestDto(
    @SerialName("reason")
    val reason: String,
)

// 통계 및 토글
@Serializable
data class MmStatisticsResponseDto(
    @SerialName("totalPurchaseOrders")
    val totalPurchaseOrders: Int,
    @SerialName("pendingRequisitions")
    val pendingRequisitions: Int,
    @SerialName("activeSuppliers")
    val activeSuppliers: Int,
    @SerialName("monthlySpending")
    val monthlySpending: Long,
)

@Serializable
data class ToggleItemDto(
    @SerialName("code")
    val code: String,
    @SerialName("name")
    val name: String,
)

