package com.autoever.everp.data.datasource.remote

import com.autoever.everp.domain.model.quotation.Quotation
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import kotlinx.coroutines.flow.Flow

interface SdRemoteDataSource {
    fun fetchQuotationList(): Result<List<QuotationListItem>>

    fun getQuotationList(): Result<List<QuotationListItem>>

    fun getQuotationDetail(quotationId: String): Result<QuotationDetail?>

    suspend fun createQuotation(quotation: Quotation): Result<Boolean>

    suspend fun updateQuotation(quotation: Quotation): Result<Boolean>

    suspend fun deleteQuotation(quotationId: Long): Result<Boolean>

    suspend fun requestQuotationApproval(quotationId: Long): Result<Boolean>
}
