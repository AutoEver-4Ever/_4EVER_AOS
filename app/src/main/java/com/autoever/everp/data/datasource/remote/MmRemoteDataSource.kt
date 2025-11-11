package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.dto.common.ToggleResponseDto
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderListItemDto
import com.autoever.everp.data.datasource.remote.http.service.SupplierDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SupplierUpdateRequestDto
import com.autoever.everp.domain.model.purchase.PurchaseOrderSearchTypeEnum
import com.autoever.everp.domain.model.purchase.PurchaseOrderStatusEnum
import java.time.LocalDate

/**
 * MM(자재 관리) 원격 데이터소스 인터페이스
 */
interface MmRemoteDataSource {
    // ========== 공급업체 ==========

    /**
     * 공급업체 상세 조회
     */
    suspend fun getSupplierDetail(
        supplierId: String,
    ): Result<SupplierDetailResponseDto>

    /**
     * 공급업체 수정
     */
    suspend fun updateSupplier(
        supplierId: String,
        request: SupplierUpdateRequestDto,
    ): Result<Unit>

    // ========== 구매 주문 ==========
    /**
     * 구매 주문 목록 조회
     */
    suspend fun getPurchaseOrderList(
        statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
        type: PurchaseOrderSearchTypeEnum = PurchaseOrderSearchTypeEnum.UNKNOWN,
        keyword: String = "",
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 0,
        size: Int = 10,
    ): Result<PageResponse<PurchaseOrderListItemDto>>

    /**
     * 구매 주문 상세 조회
     */
    suspend fun getPurchaseOrderDetail(
        purchaseOrderId: String,
    ): Result<PurchaseOrderDetailResponseDto>

    /**
     * 발주서 검색 타입 토글 조회
     */
    suspend fun getPurchaseOrderSearchTypeToggle(): Result<List<ToggleResponseDto>>

    /**
     * 발주서 상태 타입 토글 조회
     */
    suspend fun getPurchaseOrderStatusTypeToggle(): Result<List<ToggleResponseDto>>
}
