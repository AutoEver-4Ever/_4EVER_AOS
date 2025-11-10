package com.autoever.everp.domain.model.invoice

import androidx.compose.ui.graphics.Color

enum class InvoiceStatusEnum {
    UNKNOWN, // 알 수 없음, 기본값
    UNPAID, // 미지급
    PENDING, // 지급 대기
    PAID, // 지급 완료
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            UNPAID -> "미지급"
            PENDING -> "지급 대기"
            PAID -> "지급 완료"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String? = if (this == UNKNOWN) null else this.name

    /**
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 상태"
            UNPAID -> "아직 지급되지 않은 청구서"
            PENDING -> "지급 처리 중인 청구서"
            PAID -> "지급이 완료된 청구서"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            UNPAID -> Color(0xFFF44336) // Red
            PENDING -> Color(0xFFFF9800) // Orange
            PAID -> Color(0xFF4CAF50) // Green
        }

    /**
     * 상태 코드 값 (서버에서 사용하는 실제 코드값)
     */
    val code: String get() = this.name

    /**
     * 지급 완료 여부
     */
    fun isPaid(): Boolean = this == PAID

    /**
     * 처리 가능 여부 (미지급 또는 대기 상태)
     */
    fun isProcessable(): Boolean = this == UNPAID || this == PENDING

    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean = this == UNPAID || this == PENDING

    companion object {
        /**
         * 문자열을 InvoiceStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): InvoiceStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid InvoiceStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 InvoiceStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): InvoiceStatusEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 InvoiceStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: InvoiceStatusEnum = UNKNOWN,
        ): InvoiceStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환 (API 필터링용)
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 상태 목록 (UNKNOWN 제외)
         */
        fun getFilterableStatuses(): List<InvoiceStatusEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 처리 가능한 상태 목록만 반환
         */
        fun getProcessableStatuses(): List<InvoiceStatusEnum> =
            entries.filter { it.isProcessable() }
    }
}
