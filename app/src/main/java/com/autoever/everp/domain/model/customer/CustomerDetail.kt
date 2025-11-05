package com.autoever.everp.domain.model.customer

/**
 * 고객사 상세 Domain Model
 */
data class CustomerDetail(
    val customerId: String,
    val customerNumber: String,
    val customerName: String,
    val ceoName: String,
    val businessNumber: String,
    val customerStatusCode: CustomerStatusEnum,
    val contactPhone: String,
    val contactEmail: String,
    val address: String,
    val detailAddress: String?,
    val managerName: String,
    val managerPhone: String,
    val managerEmail: String,
    val totalOrders: Long,
    val totalTransactionAmount: Long,
    val note: String?,
) {
    /**
     * 활성 고객사 여부
     */
    val isActive: Boolean
        get() = customerStatusCode == CustomerStatusEnum.ACTIVE

    /**
     * 전체 주소
     */
    val fullAddress: String
        get() = if (detailAddress.isNullOrBlank()) address else "$address $detailAddress"
}

