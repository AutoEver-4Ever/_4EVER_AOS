package com.autoever.everp.domain.model.supplier

/**
 * 공급업체 상세 Domain Model
 */
data class SupplierDetail(
    val id: String,
    val name: String,
    val number: String,
    val email: String,
    val phone: String,
    val baseAddress: String,
    val detailAddress: String?,
    val status: SupplierStatusEnum,
    val category: SupplierCategoryEnum,
    val deliveryLeadTime: Int,
    val manager: SupplierManager,
) {
    /**
     * 전체 주소
     */
    val fullAddress: String
        get() = if (detailAddress.isNullOrBlank()) {
            baseAddress
        } else {
            "$baseAddress $detailAddress"
        }

    /**
     * 활성 공급업체 여부
     */
    val isActive: Boolean
        get() = status == SupplierStatusEnum.ACTIVE

    data class SupplierManager(
        val name: String,
        val phone: String,
        val email: String,
    )
}

