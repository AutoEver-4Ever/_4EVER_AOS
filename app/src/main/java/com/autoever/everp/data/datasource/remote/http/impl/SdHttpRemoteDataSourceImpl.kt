package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.QuotationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.CustomerDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.CustomerUpdateRequestDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationCreateRequestDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderListItemDto
import com.autoever.everp.data.datasource.remote.http.service.SdApi
import com.autoever.everp.domain.model.quotation.QuotationSearchTypeEnum
import com.autoever.everp.domain.model.quotation.QuotationStatusEnum
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
import com.autoever.everp.domain.model.sale.SalesOrderStatusEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

/**
 * SD(영업 관리) 원격 데이터소스 구현체
 */
class SdHttpRemoteDataSourceImpl @Inject constructor(
    private val sdApi: SdApi,
) : SdRemoteDataSource {

    // ========== 견적서 ==========
    override suspend fun getQuotationList(
        startDate: LocalDate?,
        endDate: LocalDate?,
        status: QuotationStatusEnum,
        type: QuotationSearchTypeEnum,
        search: String,
        sort: String, // Busintess/sd Quotation Enity의 sort와 동일
        page: Int,
        size: Int,
    ): Result<PageResponse<QuotationListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.getQuotationList(
                startDate = startDate,
                endDate = endDate,
                status = status.toApiString(),
                type = type.toApiString(),
                search = search.ifBlank { null },
                sort = sort,
                page = page,
                size = size,
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "견적서 목록 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "견적서 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getQuotationDetail(
        quotationId: String,
    ): Result<QuotationDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.getQuotationDetail(quotationId = quotationId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "견적서 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "견적서 상세 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun createQuotation(
        request: QuotationCreateRequestDto,
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.createQuotation(request = request)
            if (response.success && response.data != null) {
                Result.success(response.data.quotationId)
            } else {
                Result.failure(Exception(response.message ?: "견적서 생성 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "견적서 생성 실패")
            Result.failure(e)
        }
    }

    // ========== 고객사 ==========
    override suspend fun getCustomerDetail(
        customerId: String,
    ): Result<CustomerDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.getCustomerDetail(customerId = customerId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "고객사 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "고객사 상세 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun updateCustomer(
        customerId: String,
        request: CustomerUpdateRequestDto,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.updateCustomer(customerId = customerId, request = request)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "고객사 수정 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "고객사 수정 실패")
            Result.failure(e)
        }
    }

    // ========== 주문서 ==========
    override suspend fun getSalesOrderList(
        startDate: LocalDate?,
        endDate: LocalDate?,
        search: String,
        type: SalesOrderSearchTypeEnum,
        status: SalesOrderStatusEnum,
        page: Int,
        size: Int,
    ): Result<PageResponse<SalesOrderListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.getSalesOrderList(
                startDate = startDate,
                endDate = endDate,
                search = search,
                type = type.toApiString(),
                status = status.toApiString(),
                page = page,
                size = size,
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "주문서 목록 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "주문서 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getSalesOrderDetail(
        salesOrderId: String,
    ): Result<SalesOrderDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = sdApi.getSalesOrderDetail(salesOrderId = salesOrderId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "주문서 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "주문서 상세 조회 실패")
            Result.failure(e)
        }
    }
}
