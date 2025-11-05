package com.autoever.everp.data.datasource.local

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import kotlinx.coroutines.flow.Flow

/**
 * SD(영업 관리) 로컬 데이터소스 인터페이스
 */
interface SdLocalDataSource {
    // ========== 견적서 ==========
    fun observeQuotationList(): Flow<PageResponse<QuotationListItem>>
    suspend fun setQuotationList(page: PageResponse<QuotationListItem>)

    fun observeQuotationDetail(quotationId: String): Flow<QuotationDetail?>
    suspend fun setQuotationDetail(quotationId: String, detail: QuotationDetail)

    // ========== 고객사 ==========
    fun observeCustomerDetail(customerId: String): Flow<CustomerDetail?>
    suspend fun setCustomerDetail(customerId: String, detail: CustomerDetail)

    // ========== 주문서 ==========
    fun observeSalesOrderList(): Flow<PageResponse<SalesOrderListItem>>
    suspend fun setSalesOrderList(page: PageResponse<SalesOrderListItem>)

    fun observeSalesOrderDetail(salesOrderId: String): Flow<SalesOrderDetail?>
    suspend fun setSalesOrderDetail(salesOrderId: String, detail: SalesOrderDetail)

    // ========== 캐시 관리 ==========
    suspend fun clearAll()
}
