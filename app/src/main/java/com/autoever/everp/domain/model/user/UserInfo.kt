package com.autoever.everp.domain.model.user

/**
 * 사용자 정보 Domain Model
 */
data class UserInfo(
    val userId: String,
    val userName: String,
    val email: String,
    val userType: UserTypeEnum,
    val userRole: UserRoleEnum,
) {
    /**
     * 내부 직원 여부
     */
    val isInternal: Boolean
        get() = userType == UserTypeEnum.INTERNAL

    /**
     * 고객사 사용자 여부
     */
    val isCustomer: Boolean
        get() = userType == UserTypeEnum.CUSTOMER

    /**
     * 공급사 사용자 여부
     */
    val isSupplier: Boolean
        get() = userType == UserTypeEnum.SUPPLIER

    /**
     * 관리자 여부
     */
    val isAdmin: Boolean
        get() = userRole.isAdmin()
}

