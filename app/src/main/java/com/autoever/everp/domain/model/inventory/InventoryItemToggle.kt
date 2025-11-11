package com.autoever.everp.domain.model.inventory

/**
 * 재고 아이템(토글용) Domain Model
 */
data class InventoryItemToggle(
    val itemId: String,
    val itemName: String,
    val uomName: String,
    val unitPrice: Long,
//    val supplierCompanyId: String,
//    val supplierCompanyName: String,
)


