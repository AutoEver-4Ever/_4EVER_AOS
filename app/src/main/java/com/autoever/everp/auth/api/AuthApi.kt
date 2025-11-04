package com.autoever.everp.auth.api

import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.model.TokenResponse

/**
 * 인증 서버 연동 API 계약.
 * - 인가 코드 교환(Authorization Code + PKCE)
 * - 로그아웃
 */
interface AuthApi {
    suspend fun exchangeCodeForToken(
        config: AuthConfig,
        code: String,
        codeVerifier: String,
    ): TokenResponse

    suspend fun logout(accessToken: String?): Boolean
}
