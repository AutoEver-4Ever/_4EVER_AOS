package com.autoever.everp.data.datasource.local.pref

import android.content.SharedPreferences
import com.autoever.everp.data.datasource.local.TokenLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TokenPrefLocalDataSourceImpl @Inject constructor(
    private val pref: SharedPreferences,
) : TokenLocalDataSource {
    override val accessTokenFlow: Flow<String?>
        get() = TODO("Not yet implemented")

    override suspend fun getAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveAccessToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAccessToken() {
        TODO("Not yet implemented")
    }

    override val fcmTokenFlow: Flow<String?>
        get() = TODO("Not yet implemented")

    override suspend fun getFcmToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveFcmToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearFcmToken() {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

}
