package com.autoever.everp.domain.model.quotation

import java.time.LocalDate

data class QuotationCreateRequest(
    val dueDate: LocalDate,
    val items: List<QuotationCreateRequestItem>,
    val note: String = "",
) {
    data class QuotationCreateRequestItem(
        val id: String,
        val quantity: Int,
        val unitPrice: Long,
    )
}
