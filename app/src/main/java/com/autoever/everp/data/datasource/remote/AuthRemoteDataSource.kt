package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.http.service.LogoutResponseDto
import com.autoever.everp.data.datasource.remote.http.service.TokenResponseDto
import com.autoever.everp.domain.model.auth.AccessToken

interface AuthRemoteDataSource {
    suspend fun exchangeAuthCodeForToken(
        clientId: String,
        redirectUri: String,
        code: String,
        codeVerifier: String,
    ): Result<AccessToken>

    suspend fun logout(accessTokenWithBearer: String): Result<Unit>
}
