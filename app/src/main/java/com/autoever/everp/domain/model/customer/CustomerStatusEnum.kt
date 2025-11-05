package com.autoever.everp.domain.model.customer

import androidx.compose.ui.graphics.Color

enum class CustomerStatusEnum {
    ACTIVE, // 활성
    INACTIVE, // 비활성
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            ACTIVE -> "활성"
            INACTIVE -> "비활성"
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
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            ACTIVE -> "활성화된 거래 가능 고객사"
            INACTIVE -> "비활성화된 거래 불가 고객사"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            ACTIVE -> Color(0xFF4CAF50) // Green
            INACTIVE -> Color(0xFFF44336) // Red
        }

    /**
     * 상태 코드 값
     */
    val code: String get() = this.name

    /**
     * 활성 상태인지 확인
     */
    fun isActive(): Boolean = this == ACTIVE

    /**
     * 비활성 상태인지 확인
     */
    fun isInactive(): Boolean = this == INACTIVE

    /**
     * 거래 가능한 상태인지 확인
     */
    fun canTrade(): Boolean = this == ACTIVE


    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean = this == INACTIVE

    companion object {
        /**
         * 문자열을 CustomerStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): CustomerStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid CustomerStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 CustomerStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): CustomerStatusEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 CustomerStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: CustomerStatusEnum = ACTIVE,
        ): CustomerStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 상태 목록 (UNKNOWN 제외)
         */
        fun getFilterableStatuses(): List<CustomerStatusEnum> =
            entries.filter { it != ACTIVE }
    }
}
