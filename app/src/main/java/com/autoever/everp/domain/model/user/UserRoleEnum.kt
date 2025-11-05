package com.autoever.everp.domain.model.user

import androidx.compose.ui.graphics.Color

enum class UserRoleEnum {
    UNKNOWN, // 알 수 없음, 기본값
    ALL_ADMIN, ALL_USER,
    MM_USER, MM_ADMIN,
    SD_USER, SD_ADMIN,
    IM_USER, IM_ADMIN,
    FCM_USER, FCM_ADMIN,
    HRM_USER, HRM_ADMIN,
    PP_USER, PP_ADMIN,
    CUSTOMER_USER, CUSTOMER_ADMIN,
    SUPPLIER_USER, SUPPLIER_ADMIN,
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            ALL_ADMIN -> "전체 관리자"
            ALL_USER -> "전체 사용자"
            MM_USER -> "자재관리 사용자"
            MM_ADMIN -> "자재관리 관리자"
            SD_USER -> "영업관리 사용자"
            SD_ADMIN -> "영업관리 관리자"
            IM_USER -> "재고관리 사용자"
            IM_ADMIN -> "재고관리 관리자"
            FCM_USER -> "재무관리 사용자"
            FCM_ADMIN -> "재무관리 관리자"
            HRM_USER -> "인사관리 사용자"
            HRM_ADMIN -> "인사관리 관리자"
            PP_USER -> "생산관리 사용자"
            PP_ADMIN -> "생산관리 관리자"
            CUSTOMER_USER -> "고객사 사용자"
            CUSTOMER_ADMIN -> "고객사 관리자"
            SUPPLIER_USER -> "공급사 사용자"
            SUPPLIER_ADMIN -> "공급사 관리자"
        }

    /**
     * 표시 이름
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 역할 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 역할"
            ALL_ADMIN -> "모든 모듈에 대한 관리자 권한"
            ALL_USER -> "모든 모듈에 대한 일반 사용자 권한"
            MM_USER -> "자재관리 모듈 읽기 권한"
            MM_ADMIN -> "자재관리 모듈 전체 권한"
            SD_USER -> "영업관리 모듈 읽기 권한"
            SD_ADMIN -> "영업관리 모듈 전체 권한"
            IM_USER -> "재고관리 모듈 읽기 권한"
            IM_ADMIN -> "재고관리 모듈 전체 권한"
            FCM_USER -> "재무관리 모듈 읽기 권한"
            FCM_ADMIN -> "재무관리 모듈 전체 권한"
            HRM_USER -> "인사관리 모듈 읽기 권한"
            HRM_ADMIN -> "인사관리 모듈 전체 권한"
            PP_USER -> "생산관리 모듈 읽기 권한"
            PP_ADMIN -> "생산관리 모듈 전체 권한"
            CUSTOMER_USER -> "고객사 일반 사용자 권한"
            CUSTOMER_ADMIN -> "고객사 관리자 권한"
            SUPPLIER_USER -> "공급사 일반 사용자 권한"
            SUPPLIER_ADMIN -> "공급사 관리자 권한"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when {
            isAdmin() -> Color(0xFFF44336) // Red (관리자)
            isUser() -> Color(0xFF2196F3) // Blue (일반 사용자)
            else -> Color(0xFF9E9E9E) // Grey
        }

    /**
     * 역할 코드 값
     */
    val code: String get() = this.name

    /**
     * 관리자 권한 여부
     */
    fun isAdmin(): Boolean =
        this == ALL_ADMIN ||
            name.endsWith("_ADMIN")

    /**
     * 일반 사용자 권한 여부
     */
    fun isUser(): Boolean =
        this == ALL_USER ||
            name.endsWith("_USER")

    /**
     * 전체 권한 여부
     */
    fun isAllRole(): Boolean = this == ALL_ADMIN || this == ALL_USER

    /**
     * 자재관리(MM) 권한 여부
     */
    fun isMaterialManagement(): Boolean = this == MM_USER || this == MM_ADMIN

    /**
     * 영업관리(SD) 권한 여부
     */
    fun isSalesDistribution(): Boolean = this == SD_USER || this == SD_ADMIN

    /**
     * 재고관리(IM) 권한 여부
     */
    fun isInventoryManagement(): Boolean = this == IM_USER || this == IM_ADMIN

    /**
     * 재무관리(FCM) 권한 여부
     */
    fun isFinancialManagement(): Boolean = this == FCM_USER || this == FCM_ADMIN

    /**
     * 인사관리(HRM) 권한 여부
     */
    fun isHumanResourceManagement(): Boolean = this == HRM_USER || this == HRM_ADMIN

    /**
     * 생산관리(PP) 권한 여부
     */
    fun isProductionPlanning(): Boolean = this == PP_USER || this == PP_ADMIN

    /**
     * 고객사 역할 여부
     */
    fun isCustomerRole(): Boolean = this == CUSTOMER_USER || this == CUSTOMER_ADMIN

    /**
     * 공급사 역할 여부
     */
    fun isSupplierRole(): Boolean = this == SUPPLIER_USER || this == SUPPLIER_ADMIN

    /**
     * 외부 사용자 역할 여부 (고객사 또는 공급사)
     */
    fun isExternalRole(): Boolean = isCustomerRole() || isSupplierRole()

    /**
     * 내부 직원 역할 여부
     */
    fun isInternalRole(): Boolean = !isExternalRole() && this != UNKNOWN

    /**
     * 유효한 역할인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 모듈 접두사 추출 (MM, SD, IM, FCM, HRM, PP 등)
     */
    fun getModulePrefix(): String? =
        when {
            name.startsWith("ALL_") -> "ALL"
            name.startsWith("CUSTOMER_") -> "CUSTOMER"
            name.startsWith("SUPPLIER_") -> "SUPPLIER"
            else -> name.substringBefore("_").takeIf { it.length <= 3 }
        }

    companion object {
        /**
         * 문자열을 UserRoleEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): UserRoleEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid UserRoleEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 UserRoleEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): UserRoleEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 UserRoleEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: UserRoleEnum = UNKNOWN,
        ): UserRoleEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 유효한 역할 목록 (UNKNOWN 제외)
         */
        fun getValidRoles(): List<UserRoleEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 관리자 역할 목록만 반환
         */
        fun getAdminRoles(): List<UserRoleEnum> =
            entries.filter { it.isAdmin() }

        /**
         * 사용자 역할 목록만 반환
         */
        fun getUserRoles(): List<UserRoleEnum> =
            entries.filter { it.isUser() }

        /**
         * 내부 직원 역할 목록
         */
        fun getInternalRoles(): List<UserRoleEnum> =
            entries.filter { it.isInternalRole() }

        /**
         * 외부 사용자 역할 목록
         */
        fun getExternalRoles(): List<UserRoleEnum> =
            entries.filter { it.isExternalRole() }

        /**
         * 특정 모듈의 역할 목록 반환
         */
        fun getRolesByModule(module: String): List<UserRoleEnum> =
            entries.filter { it.getModulePrefix() == module.uppercase() }
    }
}
