package com.autoever.everp.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.autoever.everp.data.datasource.local.TokenLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStoreLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : TokenLocalDataSource {

    companion object {
        private const val STORE_NAME = "everp_tokens"
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

    override val accessTokenFlow: Flow<String?>
        get() = appContext.dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs -> prefs[KEY_ACCESS_TOKEN] }

    override suspend fun getAccessToken(): String? = accessTokenFlow.first()

    override suspend fun saveAccessToken(token: String) {
        appContext.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token
        }
    }

    override suspend fun clearAccessToken() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
        }
    }

    override val fcmTokenFlow: Flow<String?>
        get() = appContext.dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { prefs -> prefs[KEY_FCM_TOKEN] }

    override suspend fun getFcmToken(): String? = fcmTokenFlow.first()

    override suspend fun saveFcmToken(token: String) {
        appContext.dataStore.edit { prefs ->
            prefs[KEY_FCM_TOKEN] = token
        }
    }

    override suspend fun clearFcmToken() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(KEY_FCM_TOKEN)
        }
    }

    override suspend fun clearAll() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
            prefs.remove(KEY_FCM_TOKEN)
        }
    }
}
