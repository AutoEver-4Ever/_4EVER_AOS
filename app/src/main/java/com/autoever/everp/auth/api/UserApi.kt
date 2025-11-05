package com.autoever.everp.auth.api

import com.autoever.everp.auth.model.UserInfo

/**
 * 사용자 정보 조회 API 계약.
 */
interface UserApi {
    suspend fun getUserInfo(accessToken: String): UserInfo
}

