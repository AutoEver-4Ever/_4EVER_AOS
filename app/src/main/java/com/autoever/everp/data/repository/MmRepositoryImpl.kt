package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.MmLocalDataSource
import com.autoever.everp.data.datasource.remote.MmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.SupplierUpdateRequestDto
import com.autoever.everp.data.datasource.remote.mapper.PurchaseOrderMapper
import com.autoever.everp.data.datasource.remote.mapper.SupplierMapper
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem
import com.autoever.everp.domain.model.purchase.PurchaseOrderListParams
import com.autoever.everp.domain.model.supplier.SupplierDetail
import com.autoever.everp.domain.repository.MmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * MM(자재 관리) Repository 구현체
 * Remote → Local → Flow 패턴 구현
 */
class MmRepositoryImpl @Inject constructor(
    private val mmLocalDataSource: MmLocalDataSource,
    private val mmRemoteDataSource: MmRemoteDataSource,
) : MmRepository {

    // ========== 공급업체 ==========
    override fun observeSupplierDetail(supplierId: String): Flow<SupplierDetail?> =
        mmLocalDataSource.observeSupplierDetail(supplierId)

    override suspend fun refreshSupplierDetail(supplierId: String): Result<Unit> {
        return getSupplierDetail(supplierId).map { detail ->
            mmLocalDataSource.setSupplierDetail(supplierId, detail)
        }
    }

    override suspend fun getSupplierDetail(supplierId: String): Result<SupplierDetail> {
        return mmRemoteDataSource.getSupplierDetail(supplierId)
            .map { SupplierMapper.toDomain(it) }
    }

    override suspend fun updateSupplier(
        supplierId: String,
        request: SupplierUpdateRequestDto,
    ): Result<Unit> {
        return mmRemoteDataSource.updateSupplier(supplierId, request)
            .onSuccess {
                // 수정 성공 시 로컬 캐시 갱신
                refreshSupplierDetail(supplierId)
            }
    }

    // ========== 구매 주문 ==========
    override fun observePurchaseOrderList(): Flow<PageResponse<PurchaseOrderListItem>> =
        mmLocalDataSource.observePurchaseOrderList()

    override suspend fun refreshPurchaseOrderList(
        params: PurchaseOrderListParams,
    ): Result<Unit> = withContext(Dispatchers.Default) {
        getPurchaseOrderList(params).map { page ->
            mmLocalDataSource.setPurchaseOrderList(page)
        }
    }

    override suspend fun getPurchaseOrderList(
        params: PurchaseOrderListParams,
    ): Result<PageResponse<PurchaseOrderListItem>> = withContext(Dispatchers.Default) {
        mmRemoteDataSource.getPurchaseOrderList(
            statusCode = params.statusCode,
            type = params.type,
            keyword = params.keyword,
            startDate = params.startDate,
            endDate = params.endDate,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            PageResponse(
                content = PurchaseOrderMapper.toDomainList(dtoPage.content),
                page = dtoPage.page,
            )
        }
    }

    // ========== 구매 주문 상세 ==========
    override fun observePurchaseOrderDetail(purchaseOrderId: String): Flow<PurchaseOrderDetail?> =
        mmLocalDataSource.observePurchaseOrderDetail(purchaseOrderId)

    override suspend fun refreshPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<Unit> {
        return getPurchaseOrderDetail(purchaseOrderId).map { detail ->
            mmLocalDataSource.setPurchaseOrderDetail(purchaseOrderId, detail)
        }
    }

    override suspend fun getPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<PurchaseOrderDetail> {
        return mmRemoteDataSource.getPurchaseOrderDetail(purchaseOrderId)
            .map { PurchaseOrderMapper.toDetailDomain(it) }
    }
}
