package com.autoever.everp.domain.model.purchase

import java.time.LocalDate

/**
 * 구매 주문 리스트 아이템 Domain Model
 */
data class PurchaseOrderListItem(
    val id: String,
    val number: String,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val totalAmount: Long,
    val status: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
    val supplierName: String,
    val itemsSummary: String,
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
}
