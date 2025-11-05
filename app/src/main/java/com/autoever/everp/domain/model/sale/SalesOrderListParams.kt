package com.autoever.everp.domain.model.sale

import java.time.LocalDate

data class SalesOrderListParams(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val searchKeyword: String = "",
    val searchType: SalesOrderSearchTypeEnum = SalesOrderSearchTypeEnum.UNKNOWN,
    val statusFilter: SalesOrderStatusEnum = SalesOrderStatusEnum.UNKNOWN,
    val page: Int = 0,
    val size: Int = 20,
)
