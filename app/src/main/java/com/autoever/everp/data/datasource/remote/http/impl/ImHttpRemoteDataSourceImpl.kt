package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.ImRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.ImApi
import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImHttpRemoteDataSourceImpl @Inject constructor(
    private val imApi: ImApi,
) : ImRemoteDataSource {

    override suspend fun getItemsToggle(

    ): Result<List<InventoryItemToggle>> = withContext(Dispatchers.IO) {
        try {
            val response = imApi.getItemsToggle()
            if (response.success && response.data != null) {
                val items = response.data.products.map { dto ->
                    InventoryItemToggle(
                        itemId = dto.itemId,
                        itemName = dto.itemName,
                        uomName = dto.uomName,
                        unitPrice = dto.unitPrice.toLong(), // Double을 Long으로 변환
                    )
                }
                Result.success(items)
            } else {
                Result.failure(
                    Exception(response.message ?: "품목 목록 조회 실패")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
