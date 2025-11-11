package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.FcmRemoteDataSource
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.data.datasource.remote.http.service.FcmApi
import com.autoever.everp.data.datasource.remote.http.service.InvoiceDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceListItemDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceUpdateRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

/**
 * FCM(재무 관리) 원격 데이터소스 구현체
 */
class FcmHttpRemoteDataSourceImpl @Inject constructor(
    private val fcmApi: FcmApi,
) : FcmRemoteDataSource {

    // ========== AP 인보이스 (매입) ==========
    override suspend fun getApInvoiceList(
//        company: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        page: Int,
        size: Int,
    ): Result<PageResponse<InvoiceListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.getApInvoiceList(
//                company,
                startDate = startDate,
                endDate = endDate,
                page = page,
                size = size
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "AP 인보이스 목록 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AP 인보이스 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getApInvoiceDetail(
        invoiceId: String,
    ): Result<InvoiceDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.getApInvoiceDetail(invoiceId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "AP 인보이스 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AP 인보이스 상세 조회 실패")
            Result.failure(e)
        }
    }

    /**
     * 매입 전표 수정
     */
    override suspend fun updateApInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.updateApInvoice(invoiceId, request)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "AP 인보이스 수정 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AP 인보이스 수정 실패")
            Result.failure(e)
        }
    }

    // ========== AR 인보이스 (매출) ==========
    override suspend fun getArInvoiceList(
//        companyName: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        page: Int,
        size: Int,
    ): Result<PageResponse<InvoiceListItemDto>> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.getArInvoiceList(
//                companyName,
                startDate = startDate,
                endDate = endDate,
                page = page,
                size = size
            )
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "AR 인보이스 목록 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AR 인보이스 목록 조회 실패")
            Result.failure(e)
        }
    }

    override suspend fun getArInvoiceDetail(
        invoiceId: String
    ): Result<InvoiceDetailResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.getArInvoiceDetail(invoiceId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "AR 인보이스 상세 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AR 인보이스 상세 조회 실패")
            Result.failure(e)
        }
    }


    /**
     * 매출 전표 수정
     */
    override suspend fun updateArInvoice(
        invoiceId: String,
        request: InvoiceUpdateRequestDto,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.updateArInvoice(invoiceId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "AR 인보이스 수정 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "AR 인보이스 수정 실패")
            Result.failure(e)
        }
    }

    // ========== 인보이스 상태 수정 ==========

    /**
     * 고객사(Customer)
     * 매출 전표 상태 수정
     * 미납 -> 확인 요청
     */
    override suspend fun updateCustomerInvoiceStatus(
        invoiceId: String,
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = fcmApi.updateCustomerInvoiceStatus(invoiceId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Timber.e(e, "수취 요청 실패")
            Result.failure(e)
        }
    }

    /**
     * 공급사(Supplier)
     * 매입 전표 상태 수정
     * 확인 요청 -> 완납
     */
    override suspend fun updateSupplierInvoiceStatus(invoiceId: String): Result<Unit> {
        return try {
            val response = fcmApi.updateSupplierInvoiceStatus(invoiceId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "수취 완료 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "수취 완료 실패")
            Result.failure(e)
        }
    }
}

