package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.SdLocalDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageDto
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SD(영업 관리) 로컬 데이터소스 구현체 (인메모리)
 */
@Singleton
class SdLocalDataSourceImpl @Inject constructor() : SdLocalDataSource {

    // 견적서 목록 캐시
    private val quotationListFlow = MutableStateFlow(
        PageResponse.empty<QuotationListItem>(),
    )

    // 견적서 상세 캐시
    private val quotationDetailsFlow = MutableStateFlow<Map<String, QuotationDetail>>(emptyMap())

    // 고객사 상세 캐시
    private val customerDetailsFlow = MutableStateFlow<Map<String, CustomerDetail>>(emptyMap())

    // 주문서 목록 캐시
    private val salesOrderListFlow = MutableStateFlow(
        PageResponse.empty<SalesOrderListItem>(),
    )

    // 주문서 상세 캐시
    private val salesOrderDetailsFlow = MutableStateFlow<Map<String, SalesOrderDetail>>(emptyMap())

    // ========== 견적서 ==========
    override fun observeQuotationList(): Flow<PageResponse<QuotationListItem>> =
        quotationListFlow.asStateFlow()

    override suspend fun setQuotationList(page: PageResponse<QuotationListItem>) {
        quotationListFlow.value = page
    }

    override fun observeQuotationDetail(quotationId: String): Flow<QuotationDetail?> =
        quotationDetailsFlow.map { it[quotationId] }

    override suspend fun setQuotationDetail(quotationId: String, detail: QuotationDetail) {
        quotationDetailsFlow.value = quotationDetailsFlow.value.toMutableMap().apply {
            put(quotationId, detail)
        }
    }

    // ========== 고객사 ==========
    override fun observeCustomerDetail(customerId: String): Flow<CustomerDetail?> =
        customerDetailsFlow.map { it[customerId] }

    override suspend fun setCustomerDetail(customerId: String, detail: CustomerDetail) {
        customerDetailsFlow.value = customerDetailsFlow.value.toMutableMap().apply {
            put(customerId, detail)
        }
    }

    // ========== 주문서 ==========
    override fun observeSalesOrderList(): Flow<PageResponse<SalesOrderListItem>> =
        salesOrderListFlow.asStateFlow()

    override suspend fun setSalesOrderList(page: PageResponse<SalesOrderListItem>) {
        salesOrderListFlow.value = page
    }

    override fun observeSalesOrderDetail(salesOrderId: String): Flow<SalesOrderDetail?> =
        salesOrderDetailsFlow.map { it[salesOrderId] }

    override suspend fun setSalesOrderDetail(salesOrderId: String, detail: SalesOrderDetail) {
        salesOrderDetailsFlow.value = salesOrderDetailsFlow.value.toMutableMap().apply {
            put(salesOrderId, detail)
        }
    }

    // ========== 캐시 관리 ==========
    override suspend fun clearAll() {
        quotationListFlow.value = PageResponse.empty()
        quotationDetailsFlow.value = emptyMap()
        customerDetailsFlow.value = emptyMap()
        salesOrderListFlow.value = PageResponse.empty()
        salesOrderDetailsFlow.value = emptyMap()
    }
}
