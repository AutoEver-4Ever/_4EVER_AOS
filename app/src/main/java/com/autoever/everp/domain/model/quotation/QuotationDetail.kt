package com.autoever.everp.domain.model.quotation

import java.time.LocalDate

data class QuotationDetail(
    val id: String, // 견적서 ID
    val number: String, // 견적서 코드
    val issueDate: LocalDate, // 발행일, 견적일자
    val dueDate: LocalDate? = null, // 납기일
    val status: QuotationStatusEnum, // 상태 값은 Enum으로 따로 관리
    val totalAmount: Long, // 총 금액
    val customer: QuotationDetailCustomer,
    val items: List<QuotationDetailItem>, // 견적서 항목 리스트
) {
    data class QuotationDetailCustomer(
        val name: String, // 고객사 이름
        val ceoName: String, // 대표자 이름
    )

    data class QuotationDetailItem(
        val id: String,
        val name: String,
        val quantity: Int,
        val uomName: String,
        val unitPrice: Long,
        val totalPrice: Long,
    )
}
