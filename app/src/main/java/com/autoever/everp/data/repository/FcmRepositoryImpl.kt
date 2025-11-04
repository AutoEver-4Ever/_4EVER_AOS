package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.FcmLocalDataSource
import com.autoever.everp.data.datasource.remote.FcmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.InvoiceUpdateRequestDto
import com.autoever.everp.data.datasource.remote.mapper.InvoiceMapper
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import com.autoever.everp.domain.model.invoice.InvoiceListParams
import com.autoever.everp.domain.repository.FcmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * FCM(재무 관리) Repository 구현체
 * Remote → Local → Flow 패턴
 */
class FcmRepositoryImpl @Inject constructor(
    private val fcmFinanceLocalDataSource: FcmLocalDataSource,
    private val fcmFinanceRemoteDataSource: FcmRemoteDataSource,
) : FcmRepository {

    // ========== AP 인보이스 (매입) ==========
    override fun observeApInvoiceList(): Flow<PageResponse<InvoiceListItem>> =
        fcmFinanceLocalDataSource.observeApInvoiceList()

    override suspend fun refreshApInvoiceList(params: InvoiceListParams): Result<Unit> {
        return getApInvoiceList(params).map { page ->
            fcmFinanceLocalDataSource.setApInvoiceList(page)
        }
    }

    override suspend fun getApInvoiceList(
        params: InvoiceListParams,
    ): Result<PageResponse<InvoiceListItem>> {
        return fcmFinanceRemoteDataSource.getApInvoiceList(
//            company = params.company,
            startDate = params.startDate,
            endDate = params.endDate,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            PageResponse(
                content = InvoiceMapper.toDomainList(dtoPage.content),
                page = dtoPage.page,
            )
        }
    }

    override fun observeApInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?> =
        fcmFinanceLocalDataSource.observeApInvoiceDetail(invoiceId)

    override suspend fun refreshApInvoiceDetail(invoiceId: String): Result<Unit> {
        return getApInvoiceDetail(invoiceId).map { detail ->
            fcmFinanceLocalDataSource.setApInvoiceDetail(invoiceId, detail)
        }
    }

    override suspend fun getApInvoiceDetail(invoiceId: String): Result<InvoiceDetail> {
        return fcmFinanceRemoteDataSource.getApInvoiceDetail(invoiceId)
            .map { InvoiceMapper.toDetailDomain(it) }
    }

    override suspend fun updateApInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit> {
        return fcmFinanceRemoteDataSource.updateApInvoice(invoiceId, request)
            .onSuccess {
                // 수정 성공 시 로컬 캐시 갱신
                refreshApInvoiceDetail(invoiceId)
            }
    }

    override suspend fun requestReceivable(invoiceId: String): Result<Unit> {
        return fcmFinanceRemoteDataSource.requestReceivable(invoiceId)
    }

    // ========== AR 인보이스 (매출) ==========
    override fun observeArInvoiceList(): Flow<PageResponse<InvoiceListItem>> =
        fcmFinanceLocalDataSource.observeArInvoiceList()

    override suspend fun refreshArInvoiceList(params: InvoiceListParams): Result<Unit> {
        return getArInvoiceList(params).map { page ->
            fcmFinanceLocalDataSource.setArInvoiceList(page)
        }
    }

    override suspend fun getArInvoiceList(
        params: InvoiceListParams,
    ): Result<PageResponse<InvoiceListItem>> {
        return fcmFinanceRemoteDataSource.getArInvoiceList(
//            companyName = params.company,
            startDate = params.startDate,
            endDate = params.endDate,
            page = params.page,
            size = params.size,
        ).map { dtoPage ->
            PageResponse(
                content = InvoiceMapper.toDomainList(dtoPage.content),
                page = dtoPage.page,
            )
        }
    }

    override fun observeArInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?> =
        fcmFinanceLocalDataSource.observeArInvoiceDetail(invoiceId)

    override suspend fun refreshArInvoiceDetail(invoiceId: String): Result<Unit> {
        return getArInvoiceDetail(invoiceId).map { detail ->
            fcmFinanceLocalDataSource.setArInvoiceDetail(invoiceId, detail)
        }
    }

    override suspend fun getArInvoiceDetail(invoiceId: String): Result<InvoiceDetail> {
        return fcmFinanceRemoteDataSource.getArInvoiceDetail(invoiceId)
            .map { InvoiceMapper.toDetailDomain(it) }
    }

    override suspend fun updateArInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit> {
        return fcmFinanceRemoteDataSource.updateArInvoice(invoiceId, request)
            .onSuccess {
                // 수정 성공 시 로컬 캐시 갱신
                refreshArInvoiceDetail(invoiceId)
            }
    }

//    override suspend fun completeReceivable(invoiceId: String): Result<Unit> {
//        return fcmFinanceRemoteDataSource.completeReceivable(invoiceId)
//            .onSuccess {
//                // 완료 성공 시 로컬 캐시 갱신
//                refreshArInvoiceDetail(invoiceId)
//            }
//    }
}

