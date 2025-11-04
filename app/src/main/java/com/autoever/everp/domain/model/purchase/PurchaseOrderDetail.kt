package com.autoever.everp.domain.model.purchase

import java.time.LocalDate

/**
 * 구매 주문 상세 Domain Model
 */
data class PurchaseOrderDetail(
    val id: String,
    val number: String,
    val status: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val supplier: PurchaseOrderDetailSupplier,
    val items: List<PurchaseOrderDetailItem>,
    val totalAmount: Long,
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

    data class PurchaseOrderDetailSupplier(
        val id: String,
        val number: String,
        val name: String,
        val managerPhone: String,
        val managerEmail: String,
    )

    /**
     * 구매 주문 아이템 Domain Model
     */
    data class PurchaseOrderDetailItem(
        val id: String,
        val name: String,
        val quantity: Int,
        val uomName: String, // 단위명
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
