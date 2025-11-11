package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import kotlinx.coroutines.flow.Flow

interface ImRepository {
    fun observeItemToggleList(): Flow<List<InventoryItemToggle>>
    suspend fun refreshItemToggleList(): Result<Unit>
    suspend fun getItemToggleList(): Result<List<InventoryItemToggle>>
}
