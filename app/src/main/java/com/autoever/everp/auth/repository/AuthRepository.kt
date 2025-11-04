package com.autoever.everp.auth.repository

import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.model.TokenResponse

/**
 * 인증 도메인 저장소 계약.
 * 화면(ViewModel)에서는 이 계약에만 의존합니다.
 */
interface AuthRepository {
    suspend fun exchange(
        code: String,
        verifier: String,
        config: AuthConfig = AuthConfig.default(),
    ): TokenResponse

    suspend fun logout(accessToken: String?): Boolean
}
