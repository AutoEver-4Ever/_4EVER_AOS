package com.autoever.everp.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
    // === Access Token ===
    val accessTokenFlow: Flow<String?>
    suspend fun getAccessToken(): String?
    suspend fun saveAccessToken(token: String)
    suspend fun clearAccessToken()

    // === FCM Token ===
    val fcmTokenFlow: Flow<String?>
    suspend fun getFcmToken(): String?
    suspend fun saveFcmToken(token: String)
    suspend fun clearFcmToken()

    // === Utilities ===
    suspend fun clearAll()
}
