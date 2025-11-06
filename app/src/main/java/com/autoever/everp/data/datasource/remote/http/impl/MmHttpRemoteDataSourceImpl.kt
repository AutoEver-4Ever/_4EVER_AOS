package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.MmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.MmApi
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderListItemDto
import com.autoever.everp.data.datasource.remote.http.service.SupplierDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SupplierUpdateRequestDto
import com.autoever.everp.domain.model.purchase.PurchaseOrderSearchTypeEnum
import com.autoever.everp.domain.model.purchase.PurchaseOrderStatusEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

/**
 * MM(자재 관리) 원격 데이터소스 구현체
 */
class MmHttpRemoteDataSourceImpl @Inject constructor(
    private val mmApi: MmApi,
) : MmRemoteDataSource {

    // ========== 공급업체 ==========
    override suspend fun getSupplierDetail(
        supplierId: String,
    ): Result<SupplierDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = mmApi.getSupplierDetail(supplierId = supplierId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "공급업체 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "공급업체 상세 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun updateSupplier(
        supplierId: String,
        request: SupplierUpdateRequestDto,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = mmApi.updateSupplier(supplierId = supplierId, request = request)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "공급업체 수정 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "공급업체 수정 실패")
            Result.failure(e)
        }
    }

    // ========== 구매 주문 ==========
    override suspend fun getPurchaseOrderList(
        statusCode: PurchaseOrderStatusEnum,
        type: PurchaseOrderSearchTypeEnum,
        keyword: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
        page: Int,
        size: Int,
    ): Result<PageResponse<PurchaseOrderListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = mmApi.getPurchaseOrderList(
                statusCode = statusCode.toApiString(),
                type = type.toApiString(),
                keyword = keyword.ifBlank { null },
                startDate = startDate,
                endDate = endDate,
                page = page,
                size = size,
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "구매 주문 목록 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "구매 주문 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<PurchaseOrderDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = mmApi.getPurchaseOrderDetail(purchaseOrderId = purchaseOrderId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "구매 주문 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "구매 주문 상세 조회 실패")
            Result.failure(e)
        }
    }
}
