package com.autoever.everp.domain.model

import java.time.LocalDate

data class QuotationListItem(
    val id: Long, // 견적서 ID
    val code: String, // 견적서 코드
    val customerName: String, // 고객사 이름
    val managerName: String, // 담당자 이름
    val status: String, // 상태 값은 Enum으로 따로 관리
    val issueDate: LocalDate, // 발행일, 견적일자
    val dueDate: LocalDate, // 납기일
    val totalAmount: Int, // 총 금액
)
