package com.autoever.everp.auth.api

import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.model.TokenResponse
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * HttpURLConnection 기반 AuthApi 구현.
 * 기존 AuthService/LogoutService 로직을 통합.
 */
class HttpAuthApi : AuthApi {
    private companion object {
        const val TAG = "AuthApi"
    }

    override suspend fun exchangeAuthCodeForToken(
        config: AuthConfig,
        code: String,
        codeVerifier: String,
    ): TokenResponse = withContext(Dispatchers.IO) {
        val tokenUrl = config.tokenEndpoint
        val url = URL(tokenUrl)
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            doOutput = true
            connectTimeout = 10000
            readTimeout = 15000
        }

        val params = mapOf(
            "grant_type" to "authorization_code",
            "code" to code,
            "redirect_uri" to config.redirectUri,
            "client_id" to config.clientId,
            "code_verifier" to codeVerifier,
        )
        val body = params.entries.joinToString("&") { (k, v) ->
            "${urlEncode(k)}=${urlEncode(v)}"
        }

        try {
            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(body) }
            val status = conn.responseCode
            val stream = if (status in 200..299) conn.inputStream else conn.errorStream
            val resp = stream.bufferedReader(Charsets.UTF_8).use(BufferedReader::readText)
            if (status !in 200..299) {
                Timber.tag(TAG).e("[ERROR] 토큰 교환 실패: HTTP ${status} | ${resp}")
                throw IllegalStateException("토큰 교환 실패: HTTP ${status}")
            }
            val json = org.json.JSONObject(resp)
            val token = TokenResponse(
                accessToken = json.optString("access_token"),
                refreshToken = json.optString("refresh_token").ifBlank { null },
                tokenType = json.optString("token_type").ifBlank { null },
                expiresIn = json.optLong("expires_in").let { if (it == 0L) null else it },
                idToken = json.optString("id_token").ifBlank { null },
            )
            if (token.accessToken.isBlank()) {
                Timber.tag(TAG).e("[ERROR] 토큰 응답에 access_token이 없습니다: ${resp}")
                throw IllegalStateException("토큰 응답 파싱 실패")
            }
            Timber.tag(TAG).i("[INFO] 토큰 교환 성공")
            token
        } finally {
            conn.disconnect()
        }
    }

    override suspend fun logout(accessToken: String?): Boolean = withContext(Dispatchers.IO) {
        val url = URL(com.autoever.everp.auth.endpoint.AuthEndpoint.LOGOUT)
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
                Timber.tag(TAG).i("[INFO] 로그아웃 성공: HTTP $status")
                true
            } else {
                Timber.tag(TAG).e("[ERROR] 로그아웃 실패: HTTP $status | ${resp ?: "(no body)"}")
                false
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e("[ERROR] 로그아웃 호출 중 예외: ${e.message}")
            false
        } finally {
            conn.disconnect()
        }
    }

    private fun urlEncode(v: String): String =
        URLEncoder.encode(v, Charsets.UTF_8.name())
}
