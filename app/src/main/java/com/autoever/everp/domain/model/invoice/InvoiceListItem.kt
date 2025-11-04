package com.autoever.everp.domain.model.invoice

import java.time.LocalDate

/**
 * 인보이스 리스트 아이템 Domain Model
 */
data class InvoiceListItem(
    val id: String,
    val number: String,
    val connection: InvoiceListItemConnection,
    val totalAmount: Long,
    val dueDate: LocalDate,
    val status: InvoiceStatusEnum,
    val reference: InvoiceListItemReference,
) {
    /**
     * 만기일이 임박한지 확인 (7일 이내)
     */
    val isDueSoon: Boolean
        get() = dueDate.minusDays(7).isBefore(LocalDate.now())

    /**
     * 만기일이 지났는지 확인
     */
    val isOverdue: Boolean
        get() = dueDate.isBefore(LocalDate.now())

    data class InvoiceListItemConnection(
        val id: String,
        val number: String,
        val name: String
    )

    data class InvoiceListItemReference(
        val id: String,
        val number: String
    )

}
