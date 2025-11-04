package com.autoever.everp.auth.pkce

import android.util.Base64
import android.util.Log
import java.security.SecureRandom

/**
 * OAuth2 state 생성 유틸 (URL-safe Base64, padding 제거).
 */
object StateGenerator {
    private const val TAG = "StateGenerator"

    fun makeState(lengthBytes: Int = 64): String {
        require(lengthBytes >= 32) { "state 바이트 길이는 최소 32 이상이어야 합니다." }
        return try {
            val random = SecureRandom()
            val bytes = ByteArray(lengthBytes)
            random.nextBytes(bytes)
            val state = Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            Log.i(TAG, "[INFO] state 생성 완료 (길이: ${state.length})")
            state
        } catch (e: Exception) {
            Log.e(TAG, "[ERROR] state 생성 중 오류가 발생했습니다: ${e.message}")
            throw e
        }
    }
}
