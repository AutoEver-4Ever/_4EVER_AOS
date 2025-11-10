package com.autoever.everp.domain.model.sale

import java.time.LocalDate

/**
 * 주문서 리스트 아이템 Domain Model
 */
data class SalesOrderListItem(
    val salesOrderId: String,
    val salesOrderNumber: String,
    val customerName: String,
    val managerName: String,
    val managerPhone: String,
    val managerEmail: String,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val totalAmount: Long,
    val statusCode: SalesOrderStatusEnum,
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
 * 주문서 상세 Domain Model
 */
data class SalesOrderDetail(
    val salesOrderId: String,
    val salesOrderNumber: String,
    val orderDate: LocalDate,
    val dueDate: LocalDate,
    val statusCode: SalesOrderStatusEnum,
    val totalAmount: Long,
    val customerId: String,
    val customerName: String,
    val baseAddress: String,
    val detailAddress: String,
    val managerName: String,
    val managerPhone: String,
    val managerEmail: String,
    val items: List<SalesOrderItem>,
    val note: String?,
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
     * 전체 주소
     */
    val fullAddress: String
        get() = if (detailAddress.isBlank()) baseAddress else "$baseAddress $detailAddress"
}

/**
 * 주문서 아이템 Domain Model
 */
data class SalesOrderItem(
    val itemId: String,
    val itemName: String,
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
