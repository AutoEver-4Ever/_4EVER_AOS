package com.autoever.everp.network

import android.util.Log
import com.autoever.everp.auth.AuthEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

data class UserInfoResponse(
    val userId: String?,
    val userName: String?,
    val loginEmail: String?,
    val userRole: String?,
    val userType: String?,
)

object GWService {
    private const val TAG = "GWService"

    suspend fun getUserInfo(accessToken: String): UserInfoResponse = withContext(Dispatchers.IO) {
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
