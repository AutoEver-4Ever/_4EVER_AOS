package com.autoever.everp.domain.repository

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.SupplierUpdateRequestDto
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem
import com.autoever.everp.domain.model.purchase.PurchaseOrderListParams
import com.autoever.everp.domain.model.supplier.SupplierDetail
import kotlinx.coroutines.flow.Flow

/**
 * MM(Materials Management - 자재 관리) Repository 인터페이스
 */
interface MmRepository {
    // ========== 공급업체 ==========
    /**
     * 로컬 캐시된 공급업체 상세 관찰
     */
    fun observeSupplierDetail(supplierId: String): Flow<SupplierDetail?>

    /**
     * 원격에서 공급업체 상세 가져와 로컬 갱신
     */
    suspend fun refreshSupplierDetail(supplierId: String): Result<Unit>

    /**
     * 공급업체 상세 조회 (Remote만)
     */
    suspend fun getSupplierDetail(supplierId: String): Result<SupplierDetail>

    /**
     * 공급업체 정보 수정
     */
    suspend fun updateSupplier(
        supplierId: String,
        request: SupplierUpdateRequestDto,
    ): Result<Unit>

    // ========== 구매 주문 ==========
    /**
     * 로컬 캐시된 구매 주문 목록 관찰
     */
    fun observePurchaseOrderList(): Flow<PageResponse<PurchaseOrderListItem>>

    /**
     * 원격에서 구매 주문 목록 가져와 로컬 갱신
     */
    suspend fun refreshPurchaseOrderList(
        params: PurchaseOrderListParams,
    ): Result<Unit>

    /**
     * 구매 주문 목록 조회 (Remote만)
     */
    suspend fun getPurchaseOrderList(
        params: PurchaseOrderListParams,
    ): Result<PageResponse<PurchaseOrderListItem>>

    /**
     * 로컬 캐시된 구매 주문 상세 관찰
     */
    fun observePurchaseOrderDetail(purchaseOrderId: String): Flow<PurchaseOrderDetail?>

    /**
     * 원격에서 구매 주문 상세 가져와 로컬 갱신
     */
    suspend fun refreshPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<Unit>

    /**
     * 구매 주문 상세 조회 (Remote만)
     */
    suspend fun getPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<PurchaseOrderDetail>
}
