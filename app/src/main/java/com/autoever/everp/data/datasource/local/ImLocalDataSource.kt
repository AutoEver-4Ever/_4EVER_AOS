package com.autoever.everp.data.datasource.local

import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import kotlinx.coroutines.flow.Flow

interface ImLocalDataSource {
    fun observeItemToggleList(): Flow<List<InventoryItemToggle>>
    suspend fun setItemToggleList(items: List<InventoryItemToggle>)
    suspend fun clear()
}


