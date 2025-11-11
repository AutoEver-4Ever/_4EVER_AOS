package com.autoever.everp.domain.model.purchase

import androidx.compose.ui.graphics.Color

enum class PurchaseOrderStatusEnum {
    UNKNOWN, // 알 수 없음, 기본값
    APPROVAL, // 승인
    PENDING,  // 대기
    REJECTED, // 반려
    DELIVERING, // 배송중 -> 사용 안 함
    DELIVERED,  // 배송완료 -> 사용 안 함
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            APPROVAL -> "승인"
            PENDING -> "대기"
            REJECTED -> "반려"
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
    fun toApiString(): String? = if(this != UNKNOWN) this.name else null

    /**
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 상태"
            APPROVAL -> "발주서가 승인된 상태"
            PENDING -> "승인 대기 중인 상태"
            REJECTED -> "발주서가 반려된 상태"
            DELIVERING -> "물품이 배송 중인 상태"
            DELIVERED -> "물품 배송이 완료된 상태"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            APPROVAL -> Color(0xFF4CAF50) // Green
            PENDING -> Color(0xFFFF9800) // Orange
            REJECTED -> Color(0xFFF44336) // Red
            DELIVERING -> Color(0xFF2196F3) // Blue
            DELIVERED -> Color(0xFF00BCD4) // Cyan
        }

    /**
     * 상태 코드 값 (서버에서 사용하는 실제 코드값)
     */
    val code: String get() = this.name

    /**
     * 승인된 상태인지 확인
     */
    fun isApproved(): Boolean = this == APPROVAL

    /**
     * 대기 상태인지 확인
     */
    fun isPending(): Boolean = this == PENDING

    /**
     * 반려된 상태인지 확인
     */
    fun isRejected(): Boolean = this == REJECTED

    /**
     * 배송 관련 상태인지 확인
     */
    fun isDeliveryStatus(): Boolean = this == DELIVERING || this == DELIVERED

    /**
     * 배송 완료 상태인지 확인
     */
    fun isDelivered(): Boolean = this == DELIVERED

    /**
     * 편집 가능한 상태인지 확인
     * 대기 또는 반려 상태일 때만 편집 가능
     */
    fun isEditable(): Boolean = this == PENDING || this == REJECTED

    /**
     * 취소 가능한 상태인지 확인
     * 배송 전 상태일 때만 취소 가능
     */
    fun isCancellable(): Boolean = this == PENDING || this == APPROVAL

    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean =
        when (this) {
            APPROVAL, REJECTED, DELIVERED -> true
            else -> false
        }

    /**
     * 다음 가능한 상태 목록 반환
     */
    fun getNextPossibleStatuses(): List<PurchaseOrderStatusEnum> =
        when (this) {
            UNKNOWN -> listOf(PENDING)
            PENDING -> listOf(APPROVAL, REJECTED)
            APPROVAL -> listOf(DELIVERING)
            REJECTED -> listOf(PENDING)
            DELIVERING -> listOf(DELIVERED)
            DELIVERED -> emptyList()
        }

    /**
     * 진행률 (0~100)
     */
    fun getProgress(): Int =
        when (this) {
            UNKNOWN -> 0
            PENDING -> 20
            REJECTED -> 0
            APPROVAL -> 40
            DELIVERING -> 70
            DELIVERED -> 100
        }

    /**
     * 관련 NotificationLinkEnum 반환
     */
    fun toNotificationLink(): com.autoever.everp.domain.model.notification.NotificationLinkEnum =
        com.autoever.everp.domain.model.notification.NotificationLinkEnum.PURCHASE_ORDER

    companion object {
        /**
         * 문자열을 PurchaseOrderStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): PurchaseOrderStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid PurchaseOrderStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 PurchaseOrderStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): PurchaseOrderStatusEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 PurchaseOrderStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: PurchaseOrderStatusEnum = UNKNOWN,
        ): PurchaseOrderStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환 (API 필터링용)
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 상태 목록 (UNKNOWN 제외)
         */
        fun getFilterableStatuses(): List<PurchaseOrderStatusEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 진행 중인 상태 목록 (대기, 승인, 배송중)
         */
        fun getActiveStatuses(): List<PurchaseOrderStatusEnum> =
            listOf(PENDING, APPROVAL, DELIVERING)

        /**
         * 완료 상태 목록 (배송완료, 반려)
         */
        fun getCompletedStatuses(): List<PurchaseOrderStatusEnum> =
            listOf(DELIVERED, REJECTED)

        /**
         * 배송 관련 상태 목록
         */
        fun getDeliveryStatuses(): List<PurchaseOrderStatusEnum> =
            listOf(DELIVERING, DELIVERED)
    }
}
