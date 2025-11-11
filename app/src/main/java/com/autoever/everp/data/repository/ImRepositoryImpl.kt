package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.ImLocalDataSource
import com.autoever.everp.data.datasource.remote.ImRemoteDataSource
import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import com.autoever.everp.domain.repository.ImRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImRepositoryImpl @Inject constructor(
    private val imLocalDataSource: ImLocalDataSource,
    private val imRemoteDataSource: ImRemoteDataSource,
) : ImRepository {

    override fun observeItemToggleList(): Flow<List<InventoryItemToggle>> =
        imLocalDataSource.observeItemToggleList()

    override suspend fun refreshItemToggleList(

    ): Result<Unit> = withContext(Dispatchers.Default) {
        getItemToggleList().map { list ->
            imLocalDataSource.setItemToggleList(list)
        }
    }

    override suspend fun getItemToggleList(): Result<List<InventoryItemToggle>> {
        return imRemoteDataSource.getItemsToggle()
    }
}
