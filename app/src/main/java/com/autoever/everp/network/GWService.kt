package com.autoever.everp.network

import android.util.Log
import com.autoever.everp.auth.TokenResponse
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class UserInfoResponse(
    val userId: String?,
    val userName: String?,
    val loginEmail: String?,
    val userRole: String?,
    val userType: String?,
)

object GWService {
    private const val TAG = "GWService"

    private fun isDebug(): Boolean = try {
        val clazz = Class.forName("com.autoever.everp.BuildConfig")
        val field = clazz.getField("DEBUG")
        field.getBoolean(null)
    } catch (e: Exception) {
        Log.w(TAG, "[INFO] BuildConfig.DEBUG 확인 실패, 개발 모드로 가정합니다: ${e.message}")
        true
    }

    private fun gwBase(): String = if (isDebug()) "http://10.0.2.2:8080" else "https://api.everp.co.kr"

    suspend fun getUserInfo(accessToken: String): UserInfoResponse = withContext(Dispatchers.IO) {
        val url = URL(gwBase() + "/api/user/info")
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
            if (status !in 200..299) {
                Log.e(TAG, "[ERROR] 사용자 정보 조회 실패: HTTP ${status} | ${resp}")
                throw IllegalStateException("사용자 정보 조회 실패: HTTP ${status}")
            }
            val json = JSONObject(resp)
            UserInfoResponse(
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

