package com.autoever.everp.domain.model.supplier

import androidx.compose.ui.graphics.Color

enum class SupplierStatusEnum {
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
            ACTIVE -> "활성화된 거래 가능 공급업체"
            INACTIVE -> "비활성화된 거래 불가 공급업체"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            ACTIVE -> Color(0xFF4CAF50) // Green
            INACTIVE -> Color(0xFF9E9E9E) // Grey
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
     * 발주 가능한 상태인지 확인
     */
    fun canOrder(): Boolean = this == ACTIVE

    /**
     * 알림이 필요한 상태인지 확인
     */
    fun needsAlert(): Boolean = this == INACTIVE

    companion object {
        /**
         * 문자열을 SupplierStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): SupplierStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid SupplierStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 SupplierStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String?): SupplierStatusEnum? =
            if (value.isNullOrBlank()) {
                null
            } else {
                try {
                    valueOf(value.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            }

        /**
         * 문자열을 SupplierStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String?,
            default: SupplierStatusEnum = ACTIVE,
        ): SupplierStatusEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 활성 상태 목록
         */
        fun activeStatuses(): List<SupplierStatusEnum> = listOf(ACTIVE)

        /**
         * 모든 비활성 상태 목록
         */
        fun inactiveStatuses(): List<SupplierStatusEnum> = listOf(INACTIVE)
    }
}
