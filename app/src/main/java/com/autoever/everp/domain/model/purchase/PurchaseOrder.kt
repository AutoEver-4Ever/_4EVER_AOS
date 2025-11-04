package com.autoever.everp.domain.model.purchase

import java.time.LocalDate



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
