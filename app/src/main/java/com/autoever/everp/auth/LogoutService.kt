package com.autoever.everp.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * 로그아웃 호출 유틸리티.
 * - POST https://auth.everp.co.kr/logout
 * - Authorization: Bearer <accessToken>
 */
object LogoutService {
    private const val TAG = "LogoutService"

    /**
     * 로그아웃 요청을 보낸다. 성공 시 true, 실패 시 false 반환.
     */
    suspend fun logout(accessToken: String?): Boolean = withContext(Dispatchers.IO) {
        val url = URL(AuthEndpoint.LOGOUT)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = false
            connectTimeout = 10000
            readTimeout = 15000
            if (!accessToken.isNullOrBlank()) {
                setRequestProperty("Authorization", "Bearer $accessToken")
            }
        }
        try {
            val status = conn.responseCode
            val stream = if (status in 200..299) conn.inputStream else conn.errorStream
            val resp = stream?.bufferedReader(Charsets.UTF_8)?.use(BufferedReader::readText)
            if (status in 200..299) {
                Log.i(TAG, "[INFO] 로그아웃 성공: HTTP $status")
                true
            } else {
                Log.e(TAG, "[ERROR] 로그아웃 실패: HTTP $status | ${resp ?: "(no body)"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "[ERROR] 로그아웃 호출 중 예외: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }
}

