package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.ImRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.ImApi
import com.autoever.everp.data.datasource.remote.http.service.ItemToggleResponseDto
import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImHttpRemoteDataSourceImpl @Inject constructor(
    private val imApi: ImApi,
) : ImRemoteDataSource {

    override suspend fun getItemsToggle(

    ): Result<List<InventoryItemToggle>> = withContext(Dispatchers.IO) {
        runCatching {
            val data = imApi.getItemsToggle().data

            data?.map { dto ->
                InventoryItemToggle(
                    itemId = dto.itemId,
                    itemName = dto.itemName,
                    uomName = dto.uomName,
                    unitPrice = dto.unitPrice,
//                    supplierCompanyId = dto.supplierCompanyId,
//                    supplierCompanyName = dto.supplierCompanyName,
                )
            } ?: emptyList()
        }
    }
}
