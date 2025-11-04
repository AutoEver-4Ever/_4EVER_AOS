package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.InvoiceDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceListItemDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceUpdateRequestDto
import java.time.LocalDate

/**
 * FCM(재무 관리) 원격 데이터소스 인터페이스
 */
interface FcmRemoteDataSource {
    // ========== AP 인보이스 (매입) ==========
    suspend fun getApInvoiceList(
//        company: String = "",
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 0,
        size: Int = 20,
    ): Result<PageResponse<InvoiceListItemDto>>

    suspend fun getApInvoiceDetail(
        invoiceId: String,
    ): Result<InvoiceDetailResponseDto>

    suspend fun updateApInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit>

    suspend fun requestReceivable(
        invoiceId: String,
    ): Result<Unit>

    // ========== AR 인보이스 (매출) ==========
    suspend fun getArInvoiceList(
//        companyName: String = "",
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
        page: Int = 0,
        size: Int = 20,
    ): Result<PageResponse<InvoiceListItemDto>>

    suspend fun getArInvoiceDetail(
        invoiceId: String,
    ): Result<InvoiceDetailResponseDto>

    suspend fun updateArInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit>

//    suspend fun completeReceivable(
//        invoiceId: String,
//    ): Result<Unit>
}

