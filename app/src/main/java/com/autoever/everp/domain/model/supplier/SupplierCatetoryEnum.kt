package com.autoever.everp.domain.model.supplier

import androidx.compose.ui.graphics.Color

enum class SupplierCatetoryEnum {
    UNKNOWN, // 알 수 없음, 기본값
    MATERIAL, // 자재
    ITEMS, // 물품
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            MATERIAL -> "자재"
            ITEMS -> "물품"
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
     * 카테고리 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "카테고리 미분류"
            MATERIAL -> "생산에 사용되는 원자재 및 부품을 공급"
            ITEMS -> "완제품 및 사무용품 등을 공급"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            MATERIAL -> Color(0xFF2196F3) // Blue
            ITEMS -> Color(0xFF4CAF50) // Green
        }

    /**
     * 아이콘 이름 (Material Icons 기준)
     * TODO: 실제 사용하는 아이콘 시스템에 맞게 조정
     */
    fun iconName(): String =
        when (this) {
            UNKNOWN -> "help_outline"
            MATERIAL -> "precision_manufacturing"
            ITEMS -> "inventory_2"
        }

    /**
     * 카테고리 코드 값
     */
    val code: String get() = this.name

    /**
     * 자재 카테고리인지 확인
     */
    fun isMaterial(): Boolean = this == MATERIAL

    /**
     * 물품 카테고리인지 확인
     */
    fun isItems(): Boolean = this == ITEMS

    /**
     * 알 수 없는 카테고리인지 확인
     */
    fun isUnknown(): Boolean = this == UNKNOWN

    /**
     * 유효한 카테고리인지 확인 (UNKNOWN이 아닌지)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 생산 관련 카테고리인지 확인
     */
    fun isProductionRelated(): Boolean = this == MATERIAL

    /**
     * 재고 관리가 필요한 카테고리인지 확인
     */
    fun needsInventoryManagement(): Boolean = this == MATERIAL || this == ITEMS

    companion object {
        /**
         * 문자열을 SupplierCategoryEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): SupplierCatetoryEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid SupplierCategoryEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 SupplierCategoryEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String?): SupplierCatetoryEnum? =
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
         * 문자열을 SupplierCategoryEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String?,
            default: SupplierCatetoryEnum = UNKNOWN,
        ): SupplierCatetoryEnum = fromStringOrNull(value) ?: default

        /**
         * 유효한 카테고리 목록 (UNKNOWN 제외)
         */
        fun validCategories(): List<SupplierCatetoryEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 모든 카테고리의 한글명 목록
         */
        fun allDisplayNames(): List<String> = entries.map { it.displayName() }

        /**
         * 카테고리별 그룹핑을 위한 우선순위
         */
        fun SupplierCatetoryEnum.priority(): Int =
            when (this) {
                MATERIAL -> 1
                ITEMS -> 2
                UNKNOWN -> 999
            }
    }
}
