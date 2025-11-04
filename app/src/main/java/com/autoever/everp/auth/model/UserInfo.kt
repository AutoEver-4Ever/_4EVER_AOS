package com.autoever.everp.auth.model

/**
 * 사용자 기본 정보 도메인 모델.
 */
data class UserInfo(
    val userId: String?,
    val userName: String?,
    val loginEmail: String?,
    val userRole: String?,
    val userType: String?,
)

