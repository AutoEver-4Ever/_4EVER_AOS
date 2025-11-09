package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.ItemToggleResponseDto
import com.autoever.everp.domain.model.inventory.InventoryItemToggle

object ImMapper {
    fun toDomain(dto: ItemToggleResponseDto): InventoryItemToggle =
        InventoryItemToggle(
            itemId = dto.itemId,
            itemName = dto.itemName,
            uomName = dto.uomName,
            unitPrice = dto.unitPrice,
//            supplierCompanyId = dto.supplierCompanyId,
//            supplierCompanyName = dto.supplierCompanyName,
        )
}


