package com.autoever.everp.domain.model

data class QuotationDetail(
    val id: Long, // 견적서 ID
    val code: String, // 견적서 코드
    val customerName: String, // 고객사 이름
    val managerName: String, // 담당자 이름
    val status: String, // 상태 값은 Enum으로 따로 관리
    val issueDate: String, // 발행일, 견적일자
    val dueDate: String, // 납기일
    val totalAmount: Int, // 총 금액
    val items: List<QuotationItem>, // 견적서 항목 리스트
)
