package com.autoever.everp.domain.model.purchase

import java.time.LocalDate

/**
 * 구매 주문 목록 조회 파라미터
 */
data class PurchaseOrderListParams(
    val statusCode: PurchaseOrderStatusEnum = PurchaseOrderStatusEnum.UNKNOWN,
    val type: PurchaseOrderSearchTypeEnum = PurchaseOrderSearchTypeEnum.UNKNOWN,
    val keyword: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val page: Int = 0,
    val size: Int = 10,
)
