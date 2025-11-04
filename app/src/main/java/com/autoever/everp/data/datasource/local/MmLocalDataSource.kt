package com.autoever.everp.data.datasource.local

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem
import com.autoever.everp.domain.model.supplier.SupplierDetail
import kotlinx.coroutines.flow.Flow

/**
 * MM(자재 관리) 로컬 데이터소스 인터페이스
 */
interface MmLocalDataSource {
    // ========== 공급업체 ==========
    fun observeSupplierDetail(supplierId: String): Flow<SupplierDetail?>
    suspend fun setSupplierDetail(supplierId: String, detail: SupplierDetail)

    // ========== 구매 주문 ==========
    fun observePurchaseOrderList(): Flow<PageResponse<PurchaseOrderListItem>>
    suspend fun setPurchaseOrderList(page: PageResponse<PurchaseOrderListItem>)

    fun observePurchaseOrderDetail(purchaseOrderId: String): Flow<PurchaseOrderDetail?>
    suspend fun setPurchaseOrderDetail(purchaseOrderId: String, detail: PurchaseOrderDetail)
    suspend fun removePurchaseOrderDetail(purchaseOrderId: String)

    // ========== 캐시 관리 ==========
    suspend fun clearAll()
}
