package com.autoever.everp.domain.model.invoice

import java.time.LocalDate

/**
 * 인보이스 상세 Domain Model
 */
data class InvoiceDetail(
    val id: String,
    val number: String,
    val type: InvoiceTypeEnum,
    val status: InvoiceStatusEnum,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val connectionName: String, // 거래처명 (공급사 또는 고객사)
    val referenceNumber: String,
    val totalAmount: Long,
    val note: String = "",
    val items: List<InvoiceDetailItem>,
) {
    /**
     * 총 아이템 개수
     */
    val totalItemCount: Int
        get() = items.size

    /**
     * 총 수량
     */
    val totalQuantity: Int
        get() = items.sumOf { it.quantity }

    /**
     * AP(매입) 인보이스 여부
     */
    val isApInvoice: Boolean
        get() = type == InvoiceTypeEnum.AP

    /**
     * AR(매출) 인보이스 여부
     */
    val isArInvoice: Boolean
        get() = type == InvoiceTypeEnum.AR

    /**
     * 인보이스 아이템 Domain Model
     */
    data class InvoiceDetailItem(
        val id: String,
        val name: String,
        val quantity: Int,
        val unitOfMaterialName: String,
        val unitPrice: Long,
        val totalPrice: Long,
    ) {
        /**
         * 수량 x 단가 = 총액 검증
         */
        val isPriceValid: Boolean
            get() = quantity * unitPrice == totalPrice
    }
}


