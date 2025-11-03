package com.autoever.everp.domain.model.invoice

import androidx.compose.ui.graphics.Color

enum class InvoiceTypeEnum {
    UNKNOWN, // 알 수 없음, 기본값
    AP, // 매입 (Accounts Payable)
    AR, // 매출 (Accounts Receivable)
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            AP -> "매입"
            AR -> "매출"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 청구서 유형 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 청구서 유형"
            AP -> "매입 청구서 (지불해야 할 금액)"
            AR -> "매출 청구서 (받아야 할 금액)"
        }

    /**
     * 전체 이름 (영문 풀네임)
     */
    fun fullName(): String =
        when (this) {
            UNKNOWN -> "Unknown"
            AP -> "Accounts Payable"
            AR -> "Accounts Receivable"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            AP -> Color(0xFFF44336) // Red (지출)
            AR -> Color(0xFF4CAF50) // Green (수입)
        }

    /**
     * 청구서 유형 코드 값 (서버에서 사용하는 실제 코드값)
     */
    val code: String get() = this.name

    /**
     * 매입 청구서 여부
     */
    fun isPayable(): Boolean = this == AP

    /**
     * 매출 청구서 여부
     */
    fun isReceivable(): Boolean = this == AR

    /**
     * 유효한 청구서 유형인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 관련 NotificationLinkEnum 반환
     */
    fun toNotificationLink(): com.autoever.everp.domain.model.notification.NotificationLinkEnum =
        when (this) {
            AP -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.PURCHASE_INVOICE
            AR -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.SALES_INVOICE
            UNKNOWN -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.UNKNOWN
        }

    companion object {
        /**
         * 문자열을 InvoiceTypeEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): InvoiceTypeEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid InvoiceTypeEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 InvoiceTypeEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): InvoiceTypeEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 InvoiceTypeEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: InvoiceTypeEnum = UNKNOWN,
        ): InvoiceTypeEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환 (API 필터링용)
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 유형 목록 (UNKNOWN 제외)
         */
        fun getFilterableTypes(): List<InvoiceTypeEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 유효한 청구서 유형만 반환 (UNKNOWN 제외)
         */
        fun getValidTypes(): List<InvoiceTypeEnum> =
            entries.filter { it.isValid() }
    }
}
