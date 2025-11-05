package com.autoever.everp.domain.model.quotation

import java.time.LocalDate

data class QuotationListItem(
    val id: String, // 견적서 ID
    val number: String, // 견적서 코드
    val customer: QuotationListItemCustomer,
    val status: QuotationStatusEnum, // 상태 값은 Enum으로 따로 관리
    val dueDate: LocalDate, // 납기일
    val product: QuotationListItemProduct,
) {
    data class QuotationListItemCustomer(
        val name: String,
    )

    data class QuotationListItemProduct(
        val productId: String,
        val quantity: Int,
        val uomName: String,
    )
}


