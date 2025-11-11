package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.FcmLocalDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageDto
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FCM(재무 관리) 로컬 데이터소스 구현체
 */
@Singleton
class FcmLocalDataSourceImpl @Inject constructor() : FcmLocalDataSource {

    // AP 인보이스 캐시
    private val apInvoiceListFlow = MutableStateFlow(
        PageResponse.empty<InvoiceListItem>(),
    )
    private val apInvoiceDetailsFlow = MutableStateFlow<Map<String, InvoiceDetail>>(emptyMap())

    // AR 인보이스 캐시
    private val arInvoiceListFlow = MutableStateFlow(
        PageResponse.empty<InvoiceListItem>(),
    )
    private val arInvoiceDetailsFlow = MutableStateFlow<Map<String, InvoiceDetail>>(emptyMap())

    // ========== AP 인보이스 ==========
    override fun observeApInvoiceList(): Flow<PageResponse<InvoiceListItem>> =
        apInvoiceListFlow.asStateFlow()

    override suspend fun setApInvoiceList(page: PageResponse<InvoiceListItem>) {
        apInvoiceListFlow.value = page
    }

    override fun observeApInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?> =
        apInvoiceDetailsFlow.map { it[invoiceId] }

    override suspend fun setApInvoiceDetail(invoiceId: String, detail: InvoiceDetail) {
        apInvoiceDetailsFlow.value = apInvoiceDetailsFlow.value.toMutableMap().apply {
            put(invoiceId, detail)
        }
    }

    // ========== AR 인보이스 ==========
    override fun observeArInvoiceList(): Flow<PageResponse<InvoiceListItem>> =
        arInvoiceListFlow.asStateFlow()

    override suspend fun setArInvoiceList(page: PageResponse<InvoiceListItem>) {
        arInvoiceListFlow.value = page
    }

    override fun observeArInvoiceDetail(invoiceId: String): Flow<InvoiceDetail?> =
        arInvoiceDetailsFlow.map { it[invoiceId] }

    override suspend fun setArInvoiceDetail(invoiceId: String, detail: InvoiceDetail) {
        arInvoiceDetailsFlow.value = arInvoiceDetailsFlow.value.toMutableMap().apply {
            put(invoiceId, detail)
        }
    }

    // ========== 캐시 관리 ==========
    override suspend fun clearAll() {
        apInvoiceListFlow.value = PageResponse.empty()
        apInvoiceDetailsFlow.value = emptyMap()
        arInvoiceListFlow.value = PageResponse.empty()
        arInvoiceDetailsFlow.value = emptyMap()
    }
}

