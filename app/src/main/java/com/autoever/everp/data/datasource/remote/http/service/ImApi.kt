package com.autoever.everp.data.datasource.remote.http.service

/**
 * 재고 관리(IM, Inventory Management) API Service
 * Base URL: /scm-pp/iv
 */
interface ImApi {

    companion object {
        private const val BASE_URL = "scm-pp/iv"
    }
}

/*
// ========== 재고 아이템 관리 ==========
@GET("$BASE_URL/inventory-items")
suspend fun getInventoryItems(
    @Query("type") type: String? = null, // WAREHOUSE_NAME, ITEM_NAME
    @Query("keyword") keyword: String? = null,
    @Query("statusCode") statusCode: String = "ALL", // ALL, NORMAL, CAUTION, URGENT
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
): ApiResponse<Any>

@POST("$BASE_URL/items")
suspend fun addInventoryItem(
    @Body request: AddInventoryItemRequestDto,
): ApiResponse<Any>

@PATCH("$BASE_URL/items/{itemId}/safety-stock")
suspend fun updateSafetyStock(
    @Path("itemId") itemId: String,
    @Query("safetyStock") safetyStock: Int,
): ApiResponse<Any>

@GET("$BASE_URL/items/{itemId}")
suspend fun getInventoryItemDetail(
    @Path("itemId") itemId: String,
): ApiResponse<Any>

// ========== 부족 재고 관리 ==========
@GET("$BASE_URL/shortage")
suspend fun getShortageList(
    @Query("status") status: String? = null,
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
): ApiResponse<Any>

@GET("$BASE_URL/shortage/preview")
suspend fun getShortagePreview(): ApiResponse<Any>

@GET("$BASE_URL/shortage/count/critical/statistic")
suspend fun getCriticalShortageStatistics(): ApiResponse<Any>

// ========== 구매 주문 관리 ==========
@GET("$BASE_URL/purchase-orders/received")
suspend fun getReceivedPurchaseOrders(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
    @Query("startDate") startDate: String? = null,
    @Query("endDate") endDate: String? = null,
): ApiResponse<Any>

@GET("$BASE_URL/purchase-orders/receiving")
suspend fun getReceivingPurchaseOrders(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
): ApiResponse<Any>

// ========== 판매 주문 관리 ==========
@GET("$BASE_URL/sales-orders/production")
suspend fun getProductionSalesOrders(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
): ApiResponse<Any>

@GET("$BASE_URL/sales-orders/ready-to-ship")
suspend fun getReadyToShipSalesOrders(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 10,
): ApiResponse<Any>

@GET("$BASE_URL/sales-orders/ready-to-ship/{salesOrderId}")
suspend fun getReadyToShipDetail(
    @Path("salesOrderId") salesOrderId: String,
): ApiResponse<Any>

@GET("$BASE_URL/sales-orders/production/{salesOrderId}")
suspend fun getProductionDetail(
    @Path("salesOrderId") salesOrderId: String,
): ApiResponse<Any>

// ========== 재고 이동 ==========
@GET("$BASE_URL/stock-transfers")
suspend fun getStockTransfers(): ApiResponse<Any>

@POST("$BASE_URL/stock-transfers")
suspend fun createStockTransfer(
    @Body request: StockTransferRequestDto,
): ApiResponse<Any>

// ========== 창고 관리 ==========
@GET("$BASE_URL/warehouses")
suspend fun getWarehouses(
    @Query("page") page: Int = 0,
    @Query("size") size: Int = 20,
): ApiResponse<Any>

@GET("$BASE_URL/warehouses/{warehouseId}")
suspend fun getWarehouseDetail(
    @Path("warehouseId") warehouseId: String,
): ApiResponse<Any>

@POST("$BASE_URL/warehouses")
suspend fun createWarehouse(
    @Body request: WarehouseCreateRequestDto,
): ApiResponse<Any>

@PUT("$BASE_URL/warehouses/{warehouseId}")
suspend fun updateWarehouse(
    @Path("warehouseId") warehouseId: String,
    @Body request: WarehouseUpdateRequestDto,
): ApiResponse<Any>

@GET("$BASE_URL/warehouses/dropdown")
suspend fun getWarehouseDropdown(
    @Query("warehouseId") warehouseId: String? = null,
): ApiResponse<Any>

// ========== 통계 ==========
@GET("$BASE_URL/statistic")
suspend fun getStatistics(): ApiResponse<Any>

@GET("$BASE_URL/warehouses/statistic")
suspend fun getWarehouseStatistics(): ApiResponse<Any>

@GET("$BASE_URL/items/toggle")
suspend fun getItemsToggle(): ApiResponse<Any>

=========== 재고 관리 ==========
@Serializable
data class AddInventoryItemRequestDto(
    val data: Map<String, Any>,
)

@Serializable
data class StockTransferRequestDto(
    val data: Map<String, Any>,
)

@Serializable
data class WarehouseCreateRequestDto(
    val data: Map<String, Any>,
)

@Serializable
data class WarehouseUpdateRequestDto(
    val data: Map<String, Any>,
)
*/
