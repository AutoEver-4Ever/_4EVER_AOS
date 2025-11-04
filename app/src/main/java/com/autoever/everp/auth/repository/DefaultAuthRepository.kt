package com.autoever.everp.auth.repository

import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.model.TokenResponse
import com.autoever.everp.auth.api.AuthApi
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {
    override suspend fun exchange(code: String, verifier: String, config: AuthConfig): TokenResponse {
        return api.exchangeCodeForToken(config, code, verifier)
    }

    override suspend fun logout(accessToken: String?): Boolean {
        return api.logout(accessToken)
    }
}
