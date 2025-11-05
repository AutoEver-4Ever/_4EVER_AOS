package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.MmLocalDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageDto
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem
import com.autoever.everp.domain.model.supplier.SupplierDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MM(자재 관리) 로컬 데이터소스 구현체 (인메모리)
 */
@Singleton
class MmLocalDataSourceImpl @Inject constructor() : MmLocalDataSource {

    // 공급업체 상세 캐시
    private val supplierDetailsFlow = MutableStateFlow<Map<String, SupplierDetail>>(emptyMap())

    // 구매 주문 목록 캐시
    private val purchaseOrderListFlow = MutableStateFlow(
        PageResponse<PurchaseOrderListItem>(
            content = emptyList(),
            page = PageDto(0, 0, 0, 0, false),
        ),
    )

    // 구매 주문 상세 캐시 (Map으로 관리)
    private val purchaseOrderDetailsFlow = MutableStateFlow<Map<String, PurchaseOrderDetail>>(emptyMap())

    // ========== 공급업체 ==========
    override fun observeSupplierDetail(supplierId: String): Flow<SupplierDetail?> =
        supplierDetailsFlow.map { it[supplierId] }

    override suspend fun setSupplierDetail(supplierId: String, detail: SupplierDetail) {
        supplierDetailsFlow.value = supplierDetailsFlow.value.toMutableMap().apply {
            put(supplierId, detail)
        }
    }

    override fun observePurchaseOrderList(): Flow<PageResponse<PurchaseOrderListItem>> =
        purchaseOrderListFlow.asStateFlow()

    override suspend fun setPurchaseOrderList(page: PageResponse<PurchaseOrderListItem>) {
        purchaseOrderListFlow.value = page
    }

    override fun observePurchaseOrderDetail(purchaseOrderId: String): Flow<PurchaseOrderDetail?> =
        purchaseOrderDetailsFlow.map { it[purchaseOrderId] }

    override suspend fun setPurchaseOrderDetail(
        purchaseOrderId: String,
        detail: PurchaseOrderDetail,
    ) {
        purchaseOrderDetailsFlow.value =
            purchaseOrderDetailsFlow.value.toMutableMap().apply {
                put(purchaseOrderId, detail)
            }
    }

    override suspend fun removePurchaseOrderDetail(purchaseOrderId: String) {
        purchaseOrderDetailsFlow.value =
            purchaseOrderDetailsFlow.value.toMutableMap().apply {
                remove(purchaseOrderId)
            }
    }

    override suspend fun clearAll() {
        supplierDetailsFlow.value = emptyMap()
        purchaseOrderListFlow.value = PageResponse(
            content = emptyList(),
            page = PageDto(0, 0, 0, 0, false),
        )
        purchaseOrderDetailsFlow.value = emptyMap()
    }
}

