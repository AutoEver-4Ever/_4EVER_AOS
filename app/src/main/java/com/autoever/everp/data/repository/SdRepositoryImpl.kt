package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.domain.model.Quotation
import com.autoever.everp.domain.model.QuotationDetail
import com.autoever.everp.domain.model.QuotationListItem
import com.autoever.everp.domain.repository.SdRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SdRepositoryImpl
    @Inject
    constructor(
        private val sdRemoteDataSource: SdRemoteDataSource,
//    private val sdLocalDataSrouce: SdLocalDataSource,
    ) : SdRepository {
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
