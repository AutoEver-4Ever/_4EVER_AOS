package com.autoever.everp.domain.model.purchase

import java.time.LocalDate

/**
 * 구매 주문 리스트 아이템 Domain Model
 */
data class PurchaseOrderListItem(
    val purchaseOrderId: String,
    val purchaseOrderNumber: String,
    val supplierName: String,
    val itemsSummary: String,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val totalAmount: Int,
    val statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
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

/**
 * 구매 주문 상세 Domain Model
 */
data class PurchaseOrderDetail(
    val purchaseOrderId: String,
    val purchaseOrderNumber: String,
    val statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val supplierId: String,
    val supplierNumber: String,
    val supplierName: String,
    val managerPhone: String,
    val managerEmail: String,
    val items: List<PurchaseOrderItem>,
    val totalAmount: Int,
    val note: String = "",
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
}

/**
 * 구매 주문 아이템 Domain Model
 */
data class PurchaseOrderItem(
    val itemId: String,
    val itemName: String,
    val quantity: Int,
    val uomName: String, // 단위명
    val unitPrice: Int,
    val totalPrice: Int,
) {
    /**
     * 수량 x 단가 = 총액 검증
     */
    val isPriceValid: Boolean
        get() = quantity * unitPrice == totalPrice
}
