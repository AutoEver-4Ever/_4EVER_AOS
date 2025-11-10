package com.autoever.everp.domain.model.sale

import androidx.compose.ui.graphics.Color

enum class SalesOrderStatusEnum {
    UNKNOWN,                // 알 수 없음, 기본값
    PENDING,              // 대기
    CONFIRMED,            // 확정
    CANCELLED,             // 취소
    MATERIAL_PREPARATION,   // 자재준비
    IN_PRODUCTION,          // 생산중
    READY_FOR_SHIPMENT,     // 출하준비
    DELIVERING,             // 배송중
    DELIVERED,              // 배송완료
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            PENDING -> "대기"
            CONFIRMED -> "확정"
            CANCELLED -> "취소"
            MATERIAL_PREPARATION -> "자재준비"
            IN_PRODUCTION -> "생산중"
            READY_FOR_SHIPMENT -> "출하준비"
            DELIVERING -> "배송중"
            DELIVERED -> "배송완료"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String? = if (this != UNKNOWN) this.name else null

    /**
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 상태"
            PENDING -> "주문 접수 대기 중"
            CONFIRMED -> "주문이 확정된 상태"
            CANCELLED -> "주문이 취소된 상태"
            MATERIAL_PREPARATION -> "주문 자재를 준비하는 단계"
            IN_PRODUCTION -> "제품을 생산하는 단계"
            READY_FOR_SHIPMENT -> "출하를 준비하는 단계"
            DELIVERING -> "제품이 배송 중인 단계"
            DELIVERED -> "배송이 완료된 단계"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            PENDING -> Color(0xFFFFC107) // Amber
            CONFIRMED -> Color(0xFF8BC34A) // Light Green
            CANCELLED -> Color(0xFFF44336) // Red
            MATERIAL_PREPARATION -> Color(0xFFFF9800) // Orange
            IN_PRODUCTION -> Color(0xFF2196F3) // Blue
            READY_FOR_SHIPMENT -> Color(0xFF00BCD4) // Cyan
            DELIVERING -> Color(0xFF9C27B0) // Purple
            DELIVERED -> Color(0xFF4CAF50) // Green
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
     * 확정 상태인지 확인
     */
    fun isConfirmed(): Boolean = this == CONFIRMED

    /**
     * 취소 상태인지 확인
     */
    fun isCancelled(): Boolean = this == CANCELLED

    /**
     * 자재준비 상태인지 확인
     */
    fun isMaterialPreparation(): Boolean = this == MATERIAL_PREPARATION

    /**
     * 생산 중인지 확인
     */
    fun isInProduction(): Boolean = this == IN_PRODUCTION

    /**
     * 출하준비 상태인지 확인
     */
    fun isReadyForShipment(): Boolean = this == READY_FOR_SHIPMENT

    /**
     * 배송 중인지 확인
     */
    fun isDelivering(): Boolean = this == DELIVERING

    /**
     * 배송 완료인지 확인
     */
    fun isDelivered(): Boolean = this == DELIVERED

    /**
     * 생산 단계인지 확인 (자재준비, 생산중)
     */
    fun isProductionPhase(): Boolean = this == MATERIAL_PREPARATION || this == IN_PRODUCTION

    /**
     * 배송 단계인지 확인 (출하준비, 배송중, 배송완료)
     */
    fun isDeliveryPhase(): Boolean =
        this == READY_FOR_SHIPMENT || this == DELIVERING || this == DELIVERED

    /**
     * 진행 중인 상태인지 확인 (완료, 취소 제외)
     */
    fun isInProgress(): Boolean = this != DELIVERED && this != CANCELLED && this != UNKNOWN

    /**
     * 취소 가능한 상태인지 확인 (출하준비 전)
     */
    fun isCancellable(): Boolean =
        this == PENDING || this == CONFIRMED || this == MATERIAL_PREPARATION || this == IN_PRODUCTION

    /**
     * 유효한 상태인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 완료 상태인지 확인 (배송완료 또는 취소)
     */
    fun isCompleted(): Boolean = this == DELIVERED || this == CANCELLED

    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean = this == CONFIRMED || this == READY_FOR_SHIPMENT || this == DELIVERED || this == CANCELLED

    /**
     * 다음 가능한 상태 목록 반환
     */
    fun getNextPossibleStatuses(): List<SalesOrderStatusEnum> =
        when (this) {
            UNKNOWN -> listOf(PENDING)
            PENDING -> listOf(CONFIRMED, CANCELLED)
            CONFIRMED -> listOf(MATERIAL_PREPARATION, CANCELLED)
            CANCELLED -> emptyList()
            MATERIAL_PREPARATION -> listOf(IN_PRODUCTION, CANCELLED)
            IN_PRODUCTION -> listOf(READY_FOR_SHIPMENT, CANCELLED)
            READY_FOR_SHIPMENT -> listOf(DELIVERING)
            DELIVERING -> listOf(DELIVERED)
            DELIVERED -> emptyList()
        }

    /**
     * 진행률 (0~100)
     */
    fun getProgress(): Int =
        when (this) {
            UNKNOWN -> 0
            PENDING -> 10
            CONFIRMED -> 15
            CANCELLED -> 0
            MATERIAL_PREPARATION -> 30
            IN_PRODUCTION -> 50
            READY_FOR_SHIPMENT -> 70
            DELIVERING -> 85
            DELIVERED -> 100
        }

    /**
     * 관련 NotificationLinkEnum 반환
     */
    fun toNotificationLink(): com.autoever.everp.domain.model.notification.NotificationLinkEnum =
        com.autoever.everp.domain.model.notification.NotificationLinkEnum.SALES_ORDER

    companion object {
        /**
         * 문자열을 SalesOrderStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): SalesOrderStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid SalesOrderStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 SalesOrderStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): SalesOrderStatusEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 SalesOrderStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: SalesOrderStatusEnum = UNKNOWN,
        ): SalesOrderStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 상태 목록 (UNKNOWN 제외)
         */
        fun getFilterableStatuses(): List<SalesOrderStatusEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 진행 중인 상태 목록 (배송완료 제외)
         */
        fun getActiveStatuses(): List<SalesOrderStatusEnum> =
            listOf(MATERIAL_PREPARATION, IN_PRODUCTION, READY_FOR_SHIPMENT, DELIVERING)

        /**
         * 생산 단계 상태 목록
         */
        fun getProductionStatuses(): List<SalesOrderStatusEnum> =
            listOf(MATERIAL_PREPARATION, IN_PRODUCTION)

        /**
         * 배송 단계 상태 목록
         */
        fun getDeliveryStatuses(): List<SalesOrderStatusEnum> =
            listOf(READY_FOR_SHIPMENT, DELIVERING, DELIVERED)

        /**
         * 완료 상태 목록 (승인, 반려)
         */
        fun getCompletedStatuses(): List<SalesOrderStatusEnum> =
            listOf(DELIVERED, CANCELLED)

        /**
         * 대기/확정 상태 목록
         */
        fun getPendingStatuses(): List<SalesOrderStatusEnum> =
            listOf(PENDING, CONFIRMED)
    }
}
