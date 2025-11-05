package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.auth.AccessToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** AccessToken 관찰 */
    val accessTokenFlow: Flow<String?>

    /** 인가 코드로 토큰 교환 후 로컬 저장 */
    suspend fun loginWithAuthorizationCode(
        clientId: String,
        redirectUri: String,
        code: String,
        codeVerifier: String,
    ): Result<Unit>

    /** 저장된 토큰 조회 (type 포함 문자열: e.g., "Bearer xxxxx") */
    suspend fun getAccessTokenWithType(): String?

    /** 로그아웃: 서버 로그아웃 + 로컬 삭제 */
    suspend fun logout(): Result<Unit>
}
