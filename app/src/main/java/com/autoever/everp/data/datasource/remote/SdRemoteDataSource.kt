package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.QuotationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.CustomerDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.CustomerUpdateRequestDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationCreateRequestDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderListItemDto
import com.autoever.everp.domain.model.quotation.QuotationSearchTypeEnum
import com.autoever.everp.domain.model.quotation.QuotationStatusEnum
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
import com.autoever.everp.domain.model.sale.SalesOrderStatusEnum
import java.time.LocalDate

/**
 * SD(영업 관리) 원격 데이터소스 인터페이스
 */
interface SdRemoteDataSource {
    // ========== 견적서 ==========
    suspend fun getQuotationList(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        status: QuotationStatusEnum = QuotationStatusEnum.UNKNOWN,
        type: QuotationSearchTypeEnum = QuotationSearchTypeEnum.UNKNOWN,
        search: String = "",
        sort: String = "",
        page: Int = 0,
        size: Int = 20,
    ): Result<PageResponse<QuotationListItemDto>>

    suspend fun getQuotationDetail(
        quotationId: String,
    ): Result<QuotationDetailResponseDto>

    suspend fun createQuotation(
        request: QuotationCreateRequestDto,
    ): Result<String> // quotationId 반환

    // ========== 고객사 ==========
    suspend fun getCustomerDetail(
        customerId: String,
    ): Result<CustomerDetailResponseDto>

    suspend fun updateCustomer(
        customerId: String,
        request: CustomerUpdateRequestDto,
    ): Result<Unit>

    // ========== 주문서 ==========
    suspend fun getSalesOrderList(
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        search: String = "",
        type: SalesOrderSearchTypeEnum = SalesOrderSearchTypeEnum.UNKNOWN,
        status: SalesOrderStatusEnum = SalesOrderStatusEnum.UNKNOWN,
        page: Int = 0,
        size: Int = 20,
    ): Result<PageResponse<SalesOrderListItemDto>>

    suspend fun getSalesOrderDetail(
        salesOrderId: String,
    ): Result<SalesOrderDetailResponseDto>
}
