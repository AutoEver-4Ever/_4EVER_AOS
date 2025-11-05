package com.autoever.everp.domain.model.quotation

import java.time.LocalDate

/**
 * 견적서 목록 조회 파라미터
 */
data class QuotationListParams(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val status: QuotationStatusEnum = QuotationStatusEnum.UNKNOWN,
    val type: QuotationSearchTypeEnum = QuotationSearchTypeEnum.UNKNOWN,
    val search: String = "",
    val sort: String = "",
    val page: Int = 0,
    val size: Int = 20,
)

