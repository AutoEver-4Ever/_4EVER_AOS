package com.autoever.everp.auth

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object AuthService {
    private const val TAG = "AuthService"

    /**
     * Authorization Code + PKCE 토큰 교환 (x-www-form-urlencoded)
     */
    suspend fun exchangeCodeForToken(
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
        val body = buildFormBody(params)

        try {
            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { it.write(body) }
            val status = conn.responseCode
            val stream = if (status in 200..299) conn.inputStream else conn.errorStream
            val resp = stream.bufferedReader(Charsets.UTF_8).use(BufferedReader::readText)
            if (status !in 200..299) {
                Log.e(TAG, "[ERROR] 토큰 교환 실패: HTTP ${status} | ${resp}")
                throw IllegalStateException("토큰 교환 실패: HTTP ${status}")
            }
            val json = JSONObject(resp)
            val token = TokenResponse(
                access_token = json.optString("access_token"),
                refresh_token = json.optString("refresh_token").ifBlank { null },
                token_type = json.optString("token_type").ifBlank { null },
                expires_in = json.optLong("expires_in").let { if (it == 0L) null else it },
                id_token = json.optString("id_token").ifBlank { null },
            )
            if (token.access_token.isBlank()) {
                Log.e(TAG, "[ERROR] 토큰 응답에 access_token이 없습니다: ${resp}")
                throw IllegalStateException("토큰 응답 파싱 실패")
            }
            Log.i(TAG, "[INFO] 토큰 교환 성공")
            token
        } finally {
            conn.disconnect()
        }
    }

    private fun buildFormBody(params: Map<String, String>): String =
        params.entries.joinToString("&") { (k, v) ->
            "${urlEncode(k)}=${urlEncode(v)}"
        }

    private fun urlEncode(v: String): String =
        URLEncoder.encode(v, Charsets.UTF_8.name())
}

