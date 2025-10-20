package com.autoever.everp.data.datasource.remote

import com.autoever.everp.domain.model.quotation.Quotation
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import kotlinx.coroutines.flow.Flow

interface SdRemoteDataSource {
    fun fetchQuotationList(): Flow<List<QuotationListItem>>

    fun getQuotationList(): Flow<List<QuotationListItem>>

    fun getQuotationDetail(quotationId: String): Flow<QuotationDetail?>

    suspend fun createQuotation(quotation: Quotation): Boolean

    suspend fun updateQuotation(quotation: Quotation): Boolean

    suspend fun deleteQuotation(quotationId: Long): Boolean

    suspend fun requestQuotationApproval(quotationId: Long): Boolean
}
