package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.AuthLocalDataSource
import com.autoever.everp.data.datasource.remote.AuthRemoteDataSource
import com.autoever.everp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override val accessTokenFlow: Flow<String?>
        get() = authLocalDataSource.accessTokenFlow

    override suspend fun loginWithAuthorizationCode(
        clientId: String,
        redirectUri: String,
        code: String,
        codeVerifier: String,
    ): Result<Unit> {
        return authRemoteDataSource.exchangeAuthCodeForToken(
            clientId = clientId,
            redirectUri = redirectUri,
            code = code,
            codeVerifier = codeVerifier,
        ).mapCatching {
            authLocalDataSource.saveAccessToken(it)
        }
    }

    override suspend fun getAccessTokenWithType(): String? {
        // AuthLocalDataSource에 helper가 없으면 로컬에서 직접 조합
        val token = authLocalDataSource.getAccessToken() ?: return null
        // 기본 타입은 Bearer로 가정, 저장된 타입이 있다면 사용하도록 확장 가능
        return "Bearer $token"
    }

    override suspend fun logout(): Result<Unit> {
        val bearer = getAccessTokenWithType() ?: return Result.success(Unit).also {
            authLocalDataSource.clearAccessToken()
        }
        return authRemoteDataSource.logout(bearer).onSuccess {
            authLocalDataSource.clearAccessToken()
        }
    }
}
