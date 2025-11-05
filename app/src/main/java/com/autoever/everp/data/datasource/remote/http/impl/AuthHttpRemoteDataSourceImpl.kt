package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.auth.api.AuthApi
import com.autoever.everp.data.datasource.remote.AuthRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.TokenResponseDto
import timber.log.Timber
import javax.inject.Inject

class AuthHttpRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRemoteDataSource {
    override suspend fun exchangeAuthCodeForToken(
        clientId: String,
        redirectUri: String,
        code: String,
        codeVerifier: String,
    ): Result<TokenResponseDto> {
        return try {
            val dto = authApi.exchangeAuthCodeForToken(
                clientId = clientId,
                redirectUri = redirectUri,
                code = code,
                codeVerifier = codeVerifier,
            )
            Result.success(dto)
        } catch (e: Exception) {
            Timber.tag("Auth").e(e, "exchangeAuthCodeForToken failed")
            Result.failure(e)
        }
    }

    override suspend fun logout(accessTokenWithBearer: String): Result<Unit> {
        return try {
            val res = authApi.logout(accessToken = accessTokenWithBearer)
            if (res.success) Result.success(Unit) else Result.failure(Exception("Logout failed"))
        } catch (e: Exception) {
            Timber.tag("Auth").e(e, "logout failed")
            Result.failure(e)
        }
    }
}
