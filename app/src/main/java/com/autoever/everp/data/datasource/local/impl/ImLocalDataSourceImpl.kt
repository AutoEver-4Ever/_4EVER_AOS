package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.ImLocalDataSource
import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ImLocalDataSourceImpl @Inject constructor() : ImLocalDataSource {
    private val itemToggleFlow = MutableStateFlow<List<InventoryItemToggle>>(emptyList())

    override fun observeItemToggleList(): Flow<List<InventoryItemToggle>> = itemToggleFlow.asStateFlow()

    override suspend fun setItemToggleList(items: List<InventoryItemToggle>) {
        itemToggleFlow.value = items
    }

    override suspend fun clear() {
        itemToggleFlow.value = emptyList()
    }
}


