package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.purchase.PurchaseOrderSearchTypeEnum
import com.autoever.everp.domain.model.purchase.PurchaseOrderStatusEnum
import com.autoever.everp.domain.model.supplier.SupplierCatetoryEnum
import com.autoever.everp.domain.model.supplier.SupplierStatusEnum
import com.autoever.everp.utils.serializer.LocalDateSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Required
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
     * 발주서 목록 조회 (페이지네이션)
     */
    @GET("$BASE_URL/purchase-orders")
    suspend fun getPurchaseOrderList(
        @Query("statusCode") statusCode: String? = null,
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

    companion object {
        private const val BASE_URL = "scm-pp/mm"
    }
}

// ========== DTOs ==========

// 공급업체
@Serializable
data class SupplierDetailResponseDto(
    @SerialName("supplierInfo")
    val supplierInfo: SupplierInfoDto,
    @SerialName("managerInfo")
    val managerInfo: SupplierManagerDto,
)

@Serializable
data class SupplierInfoDto(
    @SerialName("supplierId")
    val supplierId: String,
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("supplierNumber")
    val supplierNumber: String,
    @SerialName("supplierEmail")
    val supplierEmail: String,
    @SerialName("supplierPhone")
    val supplierPhone: String,
    @SerialName("supplierBaseAddress")
    val supplierBaseAddress: String,
    @SerialName("supplierDetailAddress")
    val supplierDetailAddress: String? = null,
    @SerialName("supplierStatus")
    val supplierStatus: SupplierStatusEnum,
    @SerialName("category")
    val category: SupplierCatetoryEnum,
    @SerialName("deliveryLeadTime")
    val deliveryLeadTime: Int,
)

@Serializable
data class SupplierManagerDto(
    @SerialName("managerName")
    val managerName: String,
    @SerialName("managerPhone")
    val managerPhone: String,
    @SerialName("managerEmail")
    val managerEmail: String,
)

@Serializable
data class SupplierUpdateRequestDto(
    @SerialName("supplierName")
    val supplierName: String,
    @SerialName("supplierEmail")
    val supplierEmail: String,
    @SerialName("supplierPhone")
    val supplierPhone: String,
    @SerialName("supplierBaseAddress")
    val supplierBaseAddress: String,
//    @EncodeDefault
    @SerialName("supplierDetailAddress")
    val supplierDetailAddress: String? = null,
    @Required
    @SerialName("category")
    val category: SupplierCatetoryEnum = SupplierCatetoryEnum.MATERIAL,
    @Required
    @SerialName("statusCode")
    val statusCode: SupplierStatusEnum = SupplierStatusEnum.ACTIVE,
    @SerialName("deliveryLeadTime")
    val deliveryLeadTime: Int,
    @SerialName("managerName")
    val managerName: String,
    @SerialName("managerPhone")
    val managerPhone: String,
    @SerialName("managerEmail")
    val managerEmail: String,
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
    val statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
)

@Serializable
data class PurchaseOrderDetailResponseDto(
    @SerialName("purchaseOrderId")
    val purchaseOrderId: String,
    @SerialName("purchaseOrderNumber")
    val purchaseOrderNumber: String,
    @SerialName("statusCode")
    val statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
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
