package com.autoever.everp.domain.repository

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.InvoiceUpdateRequestDto
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import com.autoever.everp.domain.model.invoice.InvoiceListParams
import kotlinx.coroutines.flow.Flow

/**
 * FCM(Financial and Cost Management - 재무 관리) Repository 인터페이스
 */
interface FcmRepository {
    // ========== AP 인보이스 (매입) ==========
    fun observeApInvoiceList(): Flow<PageResponse<InvoiceListItem>>
    suspend fun refreshApInvoiceList(params: InvoiceListParams): Result<Unit>
    suspend fun getApInvoiceList(params: InvoiceListParams): Result<PageResponse<InvoiceListItem>>

    fun observeApInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?>
    suspend fun refreshApInvoiceDetail(invoiceId: String): Result<Unit>
    suspend fun getApInvoiceDetail(invoiceId: String): Result<InvoiceDetail>

    suspend fun updateApInvoice(invoiceId: String, request: InvoiceUpdateRequestDto): Result<Unit>
    suspend fun requestReceivable(invoiceId: String): Result<Unit>

    // ========== AR 인보이스 (매출) ==========
    fun observeArInvoiceList(): Flow<PageResponse<InvoiceListItem>>
    suspend fun refreshArInvoiceList(params: InvoiceListParams): Result<Unit>
    suspend fun getArInvoiceList(params: InvoiceListParams): Result<PageResponse<InvoiceListItem>>

    fun observeArInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?>
    suspend fun refreshArInvoiceDetail(invoiceId: String): Result<Unit>
    suspend fun getArInvoiceDetail(invoiceId: String): Result<InvoiceDetail>

    suspend fun updateArInvoice(invoiceId: String, request: InvoiceUpdateRequestDto): Result<Unit>
//    suspend fun completeReceivable(invoiceId: String): Result<Unit>
}

