package com.autoever.everp.domain.model

data class QuotationItem(
    val id: Long, // 견적서 항목 ID
    val name: String, // 항목 이름
    val quantity: Int, // 수량
    val unitPrice: Int, // 단가
    val totalPrice: Int, // 총 가격(quantity * unitPrice)
)
