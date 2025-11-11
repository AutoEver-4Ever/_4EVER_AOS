package com.autoever.everp.data.datasource.local

import com.autoever.everp.domain.model.auth.AccessToken
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    // === Access Token ===
    val accessTokenFlow: Flow<String?>
    suspend fun getAccessToken(): String?
    suspend fun saveAccessToken(token: AccessToken)
    suspend fun clearAccessToken()
}
