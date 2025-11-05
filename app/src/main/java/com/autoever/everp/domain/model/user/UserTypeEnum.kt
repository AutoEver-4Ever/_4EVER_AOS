package com.autoever.everp.domain.model.user

import androidx.compose.ui.graphics.Color

enum class UserTypeEnum {
    UNKNOWN, // 알 수 없음, 기본값
    CUSTOMER, // 고객사
    SUPPLIER, // 공급사
    INTERNAL, // 내부직원
    ;

    /**
     * 역할을 한글로 변환
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "미설정"
            CUSTOMER -> "고객사"
            SUPPLIER -> "공급사"
            INTERNAL -> "내부직원"
        }

    /**
     * 역할을 영어로 변환 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 역할 표시 이름
     */
    fun displayName(): String = toKorean()

    /**
     * 사용자 유형 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 사용자 유형"
            CUSTOMER -> "제품/서비스를 구매하는 고객사 사용자"
            SUPPLIER -> "자재/제품을 공급하는 공급사 사용자"
            INTERNAL -> "회사 내부 직원"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            CUSTOMER -> Color(0xFF2196F3) // Blue
            SUPPLIER -> Color(0xFFFF9800) // Orange
            INTERNAL -> Color(0xFF4CAF50) // Green
        }

    /**
     * 사용자 유형 코드 값
     */
    val code: String get() = this.name

    /**
     * 고객사 사용자인지 확인
     */
    fun isCustomer(): Boolean = this == CUSTOMER

    /**
     * 공급사 사용자인지 확인
     */
    fun isSupplier(): Boolean = this == SUPPLIER

    /**
     * 내부 직원인지 확인
     */
    fun isInternal(): Boolean = this == INTERNAL

    /**
     * 외부 사용자인지 확인 (고객사 또는 공급사)
     */
    fun isExternal(): Boolean = this == CUSTOMER || this == SUPPLIER

    /**
     * 유효한 사용자 유형인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 견적서 작성 권한 확인
     */
    fun canCreateQuotation(): Boolean = this == CUSTOMER || this == INTERNAL

    /**
     * 발주서 조회 권한 확인
     */
    fun canViewPurchaseOrder(): Boolean = this == SUPPLIER || this == INTERNAL

    /**
     * 주문서 조회 권한 확인
     */
    fun canViewSalesOrder(): Boolean = this == CUSTOMER || this == INTERNAL

    companion object {
        /**
         * 문자열을 UserTypeEnum로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): UserTypeEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid UserTypeEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 UserTypeEnum로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): UserTypeEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 UserTypeEnum로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: UserTypeEnum = UNKNOWN,
        ): UserTypeEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 유효한 사용자 유형 목록 (UNKNOWN 제외)
         */
        fun getValidTypes(): List<UserTypeEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 외부 사용자 유형 목록 (고객사, 공급사)
         */
        fun getExternalTypes(): List<UserTypeEnum> =
            listOf(CUSTOMER, SUPPLIER)
    }
}
