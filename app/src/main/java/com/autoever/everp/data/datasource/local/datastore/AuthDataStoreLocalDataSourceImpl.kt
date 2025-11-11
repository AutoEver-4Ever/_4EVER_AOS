package com.autoever.everp.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.autoever.everp.common.annotation.ApplicationScope
import com.autoever.everp.data.datasource.local.AuthLocalDataSource
import com.autoever.everp.domain.model.auth.AccessToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AuthDataStoreLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @ApplicationScope private val appScope: CoroutineScope,
) : AuthLocalDataSource {

    companion object {
        private const val STORE_NAME = "everp_tokens"
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_ACCESS_TOKEN_TYPE = stringPreferencesKey("access_token_type")
        private val KEY_ACCESS_TOKEN_EXPIRES_IN = stringPreferencesKey("access_token_expires_in")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

    override val accessTokenFlow: Flow<String?> = appContext.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[KEY_ACCESS_TOKEN] }
        .flowOn(Dispatchers.IO)


    override suspend fun getAccessToken(): String? = accessTokenFlow.first()

    suspend fun getAccessTokenWithType(): String? {
        // 1. dataStore에서 Preferences를 한 번만 읽어옵니다.
        val prefs = appContext.dataStore.data
            .catch { emit(emptyPreferences()) }
            .first()

        // 2. 읽어온 Preferences 객체에서 필요한 값을 모두 꺼냅니다.
        val token = prefs[KEY_ACCESS_TOKEN]
        val type = prefs[KEY_ACCESS_TOKEN_TYPE]

        return if (token != null && type != null) {
            "$type $token"
        } else {
            null
        }
    }

    override suspend fun saveAccessToken(token: AccessToken) {
        appContext.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token.token
            prefs[KEY_ACCESS_TOKEN_TYPE] = token.type
            prefs[KEY_ACCESS_TOKEN_EXPIRES_IN] = token.expiresIn.toString()
        }
    }

    override suspend fun clearAccessToken() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
            prefs.remove(KEY_ACCESS_TOKEN_TYPE)
            prefs.remove(KEY_ACCESS_TOKEN_EXPIRES_IN)
            // prefs.clear()
        }
    }

}
