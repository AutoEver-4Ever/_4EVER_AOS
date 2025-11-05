package com.autoever.everp.auth.session

import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber

/**
 * SharedPreferences 기반의 간단한 TokenStore 구현.
 * 추후 EncryptedSharedPreferences로 대체 가능.
 */
class TokenStoreImpl(
    private val prefs: SharedPreferences,
) : TokenStore {

    override fun getAccessToken(): String? {
        return try {
            val token = prefs.getString(KEY_ACCESS_TOKEN, null)
            if (token.isNullOrEmpty()) {
                Timber.tag(TAG).i("[INFO] 저장된 액세스 토큰이 없습니다.")
                null
            } else {
                Timber.tag(TAG).i("[INFO] 저장된 액세스 토큰을 불러왔습니다. (길이: ${token.length})")
                token
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 액세스 토큰 조회 중 오류가 발생했습니다: ${e.message}")
            null
        }
    }

    override fun saveAccessToken(token: String) {
        try {
            prefs.edit { putString(KEY_ACCESS_TOKEN, token) }
            Timber.tag(TAG).i("[INFO] 액세스 토큰을 저장했습니다. (길이: ${token.length})")
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 액세스 토큰 저장 중 오류가 발생했습니다: ${e.message}")
        }
    }

    override fun clear() {
        try {
            prefs.edit { remove(KEY_ACCESS_TOKEN) }
            Timber.tag(TAG).i("[INFO] 액세스 토큰을 삭제했습니다.")
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 액세스 토큰 삭제 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val TAG = "TokenStore"
    }
}
