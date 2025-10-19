package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.Quotation
import com.autoever.everp.domain.model.QuotationDetail
import com.autoever.everp.domain.model.QuotationListItem
import kotlinx.coroutines.flow.Flow

// SD : Sales and Distribution Management
// Repository for handling SD related data operations
interface SdRepository {
    fun getQuotationList(): Flow<List<QuotationListItem>>

    fun getQuotationDetail(quotationId: String): Flow<QuotationDetail?>

    suspend fun createQuotation(quotation: Quotation): Boolean

    suspend fun updateQuotation(quotation: Quotation): Boolean

    suspend fun deleteQuotation(quotationId: Long): Boolean

    suspend fun requestQuotationApproval(quotationId: Long): Boolean
}
