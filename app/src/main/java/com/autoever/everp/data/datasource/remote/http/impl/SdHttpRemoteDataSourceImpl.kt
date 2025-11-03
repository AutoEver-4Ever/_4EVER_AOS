package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.SdApi
import com.autoever.everp.domain.model.quotation.Quotation
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import javax.inject.Inject

class SdHttpRemoteDataSourceImpl @Inject constructor(
    private val sdApiService: SdApi,
) : SdRemoteDataSource {
    override fun fetchQuotationList(): Result<List<QuotationListItem>> {
        TODO("Not yet implemented")
    }

    override fun getQuotationList(): Result<List<QuotationListItem>> {
        TODO("Not yet implemented")
    }

    override fun getQuotationDetail(quotationId: String): Result<QuotationDetail?> {
        TODO("Not yet implemented")
    }

    override suspend fun createQuotation(quotation: Quotation): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuotation(quotation: Quotation): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuotation(quotationId: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun requestQuotationApproval(quotationId: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }
}
