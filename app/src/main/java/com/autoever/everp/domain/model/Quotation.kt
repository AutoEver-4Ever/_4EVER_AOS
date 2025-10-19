package com.autoever.everp.domain.model

// TODO 미정
// Quotation: 견적서 모델 클래스
data class Quotation(
    val id: Long,
    val customerName: String,
    val amount: Double,
    val validUntil: String,
)
