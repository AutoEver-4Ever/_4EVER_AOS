package com.autoever.everp.data.datasource.local

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import kotlinx.coroutines.flow.Flow

/**
 * FCM(재무 관리) 로컬 데이터소스 인터페이스
 */
interface FcmLocalDataSource {
    // ========== AP 인보이스 (매입) ==========
    fun observeApInvoiceList(): Flow<PageResponse<InvoiceListItem>>
    suspend fun setApInvoiceList(page: PageResponse<InvoiceListItem>)

    fun observeApInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?>
    suspend fun setApInvoiceDetail(invoiceId: String, detail: InvoiceDetail)

    // ========== AR 인보이스 (매출) ==========
    fun observeArInvoiceList(): Flow<PageResponse<InvoiceListItem>>
    suspend fun setArInvoiceList(page: PageResponse<InvoiceListItem>)

    fun observeArInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?>
    suspend fun setArInvoiceDetail(invoiceId: String, detail: InvoiceDetail)

    // ========== 캐시 관리 ==========
    suspend fun clearAll()
}

