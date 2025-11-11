package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.SdLocalDataSource
import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.mapper.SdMapper
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.quotation.QuotationCreateRequest
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.quotation.QuotationListParams
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import com.autoever.everp.domain.model.sale.SalesOrderListParams
import com.autoever.everp.domain.repository.SdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * SD(영업 관리) Repository 구현체
 * Remote → Local → Flow 패턴
 */
class SdRepositoryImpl @Inject constructor(
    private val sdRemoteDataSource: SdRemoteDataSource,
    private val sdLocalDataSource: SdLocalDataSource,
) : SdRepository {

    // ========== 견적서 ==========
    override fun observeQuotationList(): Flow<PageResponse<QuotationListItem>> =
        sdLocalDataSource.observeQuotationList()

    override suspend fun refreshQuotationList(
        params: QuotationListParams,
    ): Result<Unit> = withContext(Dispatchers.Default) {
        getQuotationList(params).map { page ->
            sdLocalDataSource.setQuotationList(page)
        }
    }

    override suspend fun getQuotationList(
        params: QuotationListParams,
    ): Result<PageResponse<QuotationListItem>> = withContext(Dispatchers.Default) {
        sdRemoteDataSource.getQuotationList(
            startDate = params.startDate,
            endDate = params.endDate,
            status = params.status,
            type = params.type,
            search = params.search,
            sort = params.sort,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            PageResponse(
                content = dtoPage.content.map {
                    SdMapper.quotationListItemToDomain(it)
                }, // 이미 QuotationListItem
                page = dtoPage.page,
            )
        }
    }

    override fun observeQuotationDetail(quotationId: String): Flow<QuotationDetail?> =
        sdLocalDataSource.observeQuotationDetail(quotationId)

    override suspend fun refreshQuotationDetail(quotationId: String): Result<Unit> {
        return getQuotationDetail(quotationId).map { detail ->
            sdLocalDataSource.setQuotationDetail(quotationId, detail)
        }
    }

    override suspend fun getQuotationDetail(quotationId: String): Result<QuotationDetail> {
        return sdRemoteDataSource.getQuotationDetail(quotationId)
            .map { SdMapper.quotationDetailToDomain(it) }
    }

    override suspend fun createQuotation(quotation: QuotationCreateRequest): Result<String> {
        val request = SdMapper.quotationToCreateRequest(quotation)
        return sdRemoteDataSource.createQuotation(request)
    }

    // ========== 고객사 ==========
    override fun observeCustomerDetail(customerId: String): Flow<CustomerDetail?> =
        sdLocalDataSource.observeCustomerDetail(customerId)

    override suspend fun refreshCustomerDetail(customerId: String): Result<Unit> {
        return getCustomerDetail(customerId).map { detail ->
            sdLocalDataSource.setCustomerDetail(customerId, detail)
        }
    }

    override suspend fun getCustomerDetail(customerId: String): Result<CustomerDetail> {
        return sdRemoteDataSource.getCustomerDetail(customerId)
            .map { SdMapper.customerDetailToDomain(it) }
    }

    override suspend fun updateCustomer(
        customerId: String,
        request: com.autoever.everp.data.datasource.remote.http.service.CustomerUpdateRequestDto,
    ): Result<Unit> {
        return sdRemoteDataSource.updateCustomer(customerId, request)
            .onSuccess {
                // 수정 성공 시 로컬 캐시 갱신
                refreshCustomerDetail(customerId)
            }
    }

    // ========== 주문서 ==========
    override fun observeSalesOrderList(): Flow<PageResponse<SalesOrderListItem>> =
        sdLocalDataSource.observeSalesOrderList()

    override suspend fun refreshSalesOrderList(
        params: SalesOrderListParams
    ): Result<Unit> = withContext(Dispatchers.Default) {
        getSalesOrderList(params).map { page ->
            sdLocalDataSource.setSalesOrderList(page)
        }
    }

    override suspend fun getSalesOrderList(
        params: SalesOrderListParams,
    ): Result<PageResponse<SalesOrderListItem>> = withContext(Dispatchers.Default) {
        sdRemoteDataSource.getSalesOrderList(
            startDate = params.startDate,
            endDate = params.endDate,
            search = params.searchKeyword,
            type = params.searchType,
            status = params.statusFilter,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            PageResponse(
                content = SdMapper.salesOrderListToDomainList(dtoPage.content),
                page = dtoPage.page,
            )
        }
    }

    override fun observeSalesOrderDetail(salesOrderId: String): Flow<SalesOrderDetail?> =
        sdLocalDataSource.observeSalesOrderDetail(salesOrderId)

    override suspend fun refreshSalesOrderDetail(salesOrderId: String): Result<Unit> {
        return getSalesOrderDetail(salesOrderId).map { detail ->
            sdLocalDataSource.setSalesOrderDetail(salesOrderId, detail)
        }
    }

    override suspend fun getSalesOrderDetail(salesOrderId: String): Result<SalesOrderDetail> {
        return sdRemoteDataSource.getSalesOrderDetail(salesOrderId)
            .map { SdMapper.salesOrderDetailToDomain(it) }
    }
}
