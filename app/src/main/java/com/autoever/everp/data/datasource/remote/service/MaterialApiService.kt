package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import retrofit2.http.*

/**
 * 자재 관리 API Service
 * Base URL: /business/hrm
 */
interface MaterialApiService {

    // ========== 공급업체 관리 ==========
    @GET("$BASE_URL/supplier")
    suspend fun getSupplierList(
        @Query("statusCode") statusCode: String = "ALL",
        @Query("category") category: String = "ALL",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
    ): ApiResponse<Any>

    @GET("$BASE_URL/supplier/{supplierId}")
    suspend fun getSupplierDetail(
        @Path("supplierId") supplierId: String,
    ): ApiResponse<Any>

    @POST("$BASE_URL/supplier")
    suspend fun createSupplier(
        @Body request: SupplierCreateRequestDto,
    ): ApiResponse<CreateAuthUserResultDto>

    // ========== 구매 요청 관리 ==========
    @POST("$BASE_URL/stock-purchase-requisitions")
    suspend fun createStockPurchaseRequisition(
        @Body request: StockPurchaseRequestDto,
    ): ApiResponse<Any>

    @GET("$BASE_URL/purchase-requisitions")
    suspend fun getPurchaseRequisitionList(
        @Query("statusCode") statusCode: String = "ALL",
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): ApiResponse<Any>

    @GET("$BASE_URL/purchase-requisitions/{purchaseRequisitionId}")
    suspend fun getPurchaseRequisitionDetail(
        @Path("purchaseRequisitionId") purchaseRequisitionId: String,
    ): ApiResponse<Any>

    @POST("$BASE_URL/purchase-requisitions")
    suspend fun createPurchaseRequisition(
        @Body request: PurchaseRequisitionCreateRequestDto,
    ): ApiResponse<Any>

    @POST("$BASE_URL/purchase-requisitions/{purchaseRequisitionId}/approve")
    suspend fun approvePurchaseRequisition(
        @Path("purchaseRequisitionId") purchaseRequisitionId: String,
    ): ApiResponse<Any>

    @POST("$BASE_URL/purchase-requisitions/{purchaseRequisitionId}/reject")
    suspend fun rejectPurchaseRequisition(
        @Path("purchaseRequisitionId") purchaseRequisitionId: String,
        @Body request: PurchaseRequisitionRejectRequestDto,
    ): ApiResponse<Any>

    // ========== 구매 주문 관리 ==========
    @GET("$BASE_URL/purchase-orders")
    suspend fun getPurchaseOrderList(
        @Query("statusCode") statusCode: String = "ALL",
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
    ): ApiResponse<Any>

    @GET("$BASE_URL/purchase-orders/{purchaseOrderId}")
    suspend fun getPurchaseOrderDetail(
        @Path("purchaseOrderId") purchaseOrderId: String,
    ): ApiResponse<Any>

    @POST("$BASE_URL/purchase-orders/{purchaseOrderId}/approve")
    suspend fun approvePurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
    ): ApiResponse<Any>

    @POST("$BASE_URL/purchase-orders/{purchaseOrderId}/reject")
    suspend fun rejectPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Body request: PurchaseOrderRejectRequestDto,
    ): ApiResponse<Any>

    // ========== 통계 및 상태 ==========
    @GET("$BASE_URL/statistics")
    suspend fun getStatistics(): ApiResponse<Any>

    @GET("$BASE_URL/purchase-requisition/status/toggle")
    suspend fun getPurchaseRequisitionStatusToggle(): ApiResponse<Any>

    @GET("$BASE_URL/purchase-orders/status/toggle")
    suspend fun getPurchaseOrderStatusToggle(): ApiResponse<Any>

    @GET("$BASE_URL/supplier/status/toggle")
    suspend fun getSupplierStatusToggle(): ApiResponse<Any>

    @GET("$BASE_URL/supplier/category/toggle")
    suspend fun getSupplierCategoryToggle(): ApiResponse<Any>

    companion object {
        private const val BASE_URL = "/business/hrm"
    }
}

@kotlinx.serialization.Serializable
data class SupplierCreateRequestDto(
    val data: Map<String, Any>,
)

@kotlinx.serialization.Serializable
data class StockPurchaseRequestDto(
    val data: Map<String, Any>,
)

@kotlinx.serialization.Serializable
data class PurchaseRequisitionCreateRequestDto(
    val data: Map<String, Any>,
)

@kotlinx.serialization.Serializable
data class PurchaseRequisitionRejectRequestDto(
    val reason: String,
)

@kotlinx.serialization.Serializable
data class PurchaseOrderRejectRequestDto(
    val reason: String,
)

