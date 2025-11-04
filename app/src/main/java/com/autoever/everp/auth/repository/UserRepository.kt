package com.autoever.everp.auth.repository

import com.autoever.everp.auth.model.UserInfo

/**
 * 사용자 정보 도메인 저장소 계약.
 */
interface UserRepository {
    suspend fun fetchUserInfo(accessToken: String): UserInfo
}

