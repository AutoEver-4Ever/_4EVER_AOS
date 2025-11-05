package com.autoever.everp.domain.model.invoice

import java.time.LocalDate

/**
 * 인보이스 목록 조회 파라미터
 */
data class InvoiceListParams(
//    val company: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val page: Int = 0,
    val size: Int = 20,
)
