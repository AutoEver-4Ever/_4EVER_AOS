package com.autoever.everp.auth.api

import android.util.Log
import com.autoever.everp.auth.endpoint.AuthEndpoint
import com.autoever.everp.auth.model.UserInfo
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * HttpURLConnection 기반 UserApi 구현.
 * 기존 GWService.getUserInfo와 동일한 동작을 제공한다.
 */
class HttpUserApi : UserApi {
    private companion object { const val TAG = "UserApi" }

    override suspend fun getUserInfo(accessToken: String): UserInfo = withContext(Dispatchers.IO) {
        val url = URL(AuthEndpoint.USER_INFO)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer $accessToken")
            connectTimeout = 10000
            readTimeout = 15000
        }
        try {
            val status = conn.responseCode
            val stream = if (status in 200..299) conn.inputStream else conn.errorStream
            val resp = stream.bufferedReader(Charsets.UTF_8).use(BufferedReader::readText)
            if (status == 401) {
                Log.e(TAG, "[ERROR] 사용자 정보 조회 실패: HTTP ${status} | ${resp}")
                throw UnauthorizedException("HTTP 401")
            }
            if (status !in 200..299) {
                Log.e(TAG, "[ERROR] 사용자 정보 조회 실패: HTTP ${status} | ${resp}")
                throw IllegalStateException("사용자 정보 조회 실패: HTTP ${status}")
            }
            // API 응답은 { success, message, data: { ... } } 형태일 수 있으므로 data 객체를 우선 시도
            val root = JSONObject(resp)
            val json = root.optJSONObject("data") ?: root
            UserInfo(
                userId = json.optString("userId").ifBlank { null },
                userName = json.optString("userName").ifBlank { null },
                loginEmail = json.optString("loginEmail").ifBlank { null },
                userRole = json.optString("userRole").ifBlank { null },
                userType = json.optString("userType").ifBlank { null },
            )
        } finally {
            conn.disconnect()
        }
    }
}
