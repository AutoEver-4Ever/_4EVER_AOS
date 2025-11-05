package com.autoever.everp.domain.repository

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.quotation.QuotationCreateRequest
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.quotation.QuotationListParams
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import com.autoever.everp.domain.model.sale.SalesOrderListParams
import kotlinx.coroutines.flow.Flow

/**
 * SD(Sales & Distribution - 영업 관리) Repository 인터페이스
 */
interface SdRepository {
    // ========== 견적서 ==========
    fun observeQuotationList(): Flow<PageResponse<QuotationListItem>>
    suspend fun refreshQuotationList(params: QuotationListParams): Result<Unit>
    suspend fun getQuotationList(params: QuotationListParams): Result<PageResponse<QuotationListItem>>

    fun observeQuotationDetail(quotationId: String): Flow<QuotationDetail?>
    suspend fun refreshQuotationDetail(quotationId: String): Result<Unit>
    suspend fun getQuotationDetail(quotationId: String): Result<QuotationDetail>

    suspend fun createQuotation(quotation: QuotationCreateRequest): Result<String> // quotationId 반환

    // ========== 고객사 ==========
    fun observeCustomerDetail(customerId: String): Flow<CustomerDetail?>
    suspend fun refreshCustomerDetail(customerId: String): Result<Unit>
    suspend fun getCustomerDetail(customerId: String): Result<CustomerDetail>

    // ========== 주문서 ==========
    fun observeSalesOrderList(): Flow<PageResponse<SalesOrderListItem>>
    suspend fun refreshSalesOrderList(params: SalesOrderListParams): Result<Unit>
    suspend fun getSalesOrderList(params: SalesOrderListParams): Result<PageResponse<SalesOrderListItem>>

    fun observeSalesOrderDetail(salesOrderId: String): Flow<SalesOrderDetail?>
    suspend fun refreshSalesOrderDetail(salesOrderId: String): Result<Unit>
    suspend fun getSalesOrderDetail(salesOrderId: String): Result<SalesOrderDetail>
}
