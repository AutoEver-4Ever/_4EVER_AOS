package com.autoever.everp.domain.model.sale

data class SalesOrderListParams(
    val page: Int = 0,
    val size: Int = 20,
    val searchType: SalesOrderSearchTypeEnum = SalesOrderSearchTypeEnum.UNKNOWN,
    val searchKeyword: String = "",
    val statusFilter: SalesOrderStatusEnum = SalesOrderStatusEnum.UNKNOWN,
)
