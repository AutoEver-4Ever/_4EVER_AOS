package com.autoever.everp.domain.repository

interface PushNotificationRepository {
    suspend fun getToken(): String

    suspend fun syncTokenWithServer(token: String)
}
