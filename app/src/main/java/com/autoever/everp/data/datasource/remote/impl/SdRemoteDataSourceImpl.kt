package com.autoever.everp.data.datasource.remote.impl

import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.service.SdApiService
import com.autoever.everp.domain.model.Quotation
import com.autoever.everp.domain.model.QuotationDetail
import com.autoever.everp.domain.model.QuotationListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SdRemoteDataSourceImpl
    @Inject
    constructor(
        private val sdApiService: SdApiService,
    ) : SdRemoteDataSource {
        override fun fetchQuotationList(): Flow<List<QuotationListItem>> {
            TODO("Not yet implemented")
        }

        override fun getQuotationList(): Flow<List<QuotationListItem>> {
            TODO("Not yet implemented")
        }

        override fun getQuotationDetail(quotationId: String): Flow<QuotationDetail?> {
            TODO("Not yet implemented")
        }

        override suspend fun createQuotation(quotation: Quotation): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun updateQuotation(quotation: Quotation): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun deleteQuotation(quotationId: Long): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun requestQuotationApproval(quotationId: Long): Boolean {
            TODO("Not yet implemented")
        }
    }
