package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.http.service.ItemToggleResponseDto
import com.autoever.everp.domain.model.inventory.InventoryItemToggle

interface ImRemoteDataSource {
    suspend fun getItemsToggle(): Result<List<InventoryItemToggle>>
}
