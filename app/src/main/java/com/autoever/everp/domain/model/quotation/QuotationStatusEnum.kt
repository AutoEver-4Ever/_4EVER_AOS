package com.autoever.everp.domain.model.quotation

import androidx.compose.ui.graphics.Color

enum class QuotationStatusEnum {
    UNKNOWN, // 알 수 없음, 기본값
    PENDING, // 대기
    REVIEW, // 검토
    APPROVED, // 승인
    REJECTED, // 반려
    ;

    /**
     * UI 표시용 한글명
     */
    fun displayName(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            PENDING -> "대기"
            REVIEW -> "검토"
            APPROVED -> "승인"
            REJECTED -> "반려"
        }

    /**
     * 상태를 한글로 변환
     */
    fun toKorean(): String = displayName()

    /**
     * Enum을 문자열로 변환 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 상태"
            PENDING -> "견적서 작성 완료, 검토 대기 중"
            REVIEW -> "견적서 검토 진행 중"
            APPROVED -> "견적서가 승인되어 주문 전환 가능"
            REJECTED -> "견적서가 반려됨"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            PENDING -> Color(0xFFFF9800) // Orange
            REVIEW -> Color(0xFF2196F3) // Blue
            APPROVED -> Color(0xFF4CAF50) // Green
            REJECTED -> Color(0xFFF44336) // Red
        }

    /**
     * 상태 코드 값
     */
    val code: String get() = this.name

    /**
     * 대기 상태인지 확인
     */
    fun isPending(): Boolean = this == PENDING

    /**
     * 검토 중인지 확인
     */
    fun isReview(): Boolean = this == REVIEW

    /**
     * 승인된 상태인지 확인
     */
    fun isApproved(): Boolean = this == APPROVED

    /**
     * 반려된 상태인지 확인
     */
    fun isRejected(): Boolean = this == REJECTED

    /**
     * 편집 가능한 상태인지 확인 (대기 또는 반려 상태)
     */
    fun isEditable(): Boolean = this == PENDING || this == REJECTED

    /**
     * 검토 요청 가능한 상태인지 확인
     */
    fun canRequestReview(): Boolean = this == PENDING

    /**
     * 승인/반려 가능한 상태인지 확인
     */
    fun canApproveOrReject(): Boolean = this == REVIEW

    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean = this == APPROVED || this == REJECTED

    /**
     * 다음 가능한 상태 목록 반환
     */
    fun getNextPossibleStatuses(): List<QuotationStatusEnum> =
        when (this) {
            UNKNOWN -> listOf(PENDING)
            PENDING -> listOf(REVIEW)
            REVIEW -> listOf(APPROVED, REJECTED)
            APPROVED -> emptyList()
            REJECTED -> listOf(PENDING)
        }

    /**
     * 진행률 (0~100)
     */
    fun getProgress(): Int =
        when (this) {
            UNKNOWN -> 0
            PENDING -> 25
            REVIEW -> 50
            APPROVED -> 100
            REJECTED -> 0
        }

    /**
     * 관련 NotificationLinkEnum 반환
     */
    fun toNotificationLink(): com.autoever.everp.domain.model.notification.NotificationLinkEnum =
        com.autoever.everp.domain.model.notification.NotificationLinkEnum.QUOTATION

    companion object {
        /**
         * 문자열을 Enum으로 변환
         * 매핑되지 않는 값이면 IllegalArgumentException 발생
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): QuotationStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid QuotationStatus: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 Enum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): QuotationStatusEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 Enum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: QuotationStatusEnum = PENDING,
        ): QuotationStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 상태 목록 (UNKNOWN 제외)
         */
        fun getFilterableStatuses(): List<QuotationStatusEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 진행 중인 상태 목록 (대기, 검토)
         */
        fun getActiveStatuses(): List<QuotationStatusEnum> =
            listOf(PENDING, REVIEW)

        /**
         * 완료 상태 목록 (승인, 반려)
         */
        fun getCompletedStatuses(): List<QuotationStatusEnum> =
            listOf(APPROVED, REJECTED)
    }
}
