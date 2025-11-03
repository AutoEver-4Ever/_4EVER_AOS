package com.autoever.everp.auth

import android.net.Uri
import android.util.Log
import com.autoever.everp.BuildConfig

/**
 * OAuth2 Authorization Code + PKCE 설정 및 인가 URL 생성 유틸.
 */
data class AuthConfig(
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val clientId: String,
    val redirectUri: String,
    val scopes: List<String>,
) {
    val redirectURL: Uri
        get() = try {
            Uri.parse(redirectUri)
        } catch (e: Exception) {
            Log.e(TAG, "[ERROR] 검증되지 않은 Redirect URI: $redirectUri")
            throw IllegalArgumentException("검증되지 않은 Redirect URI 형식입니다.")
        }

    val redirectScheme: String? get() = redirectURL.scheme
    val redirectHost: String? get() = redirectURL.host
    val redirectPath: String get() = redirectURL.path ?: ""

    /**
     * 인가 요청 URI 생성
     * - response_type=code
     * - client_id, redirect_uri, scope, code_challenge, state, code_challenge_method=S256
     */
    fun buildAuthorizationUri(
        codeChallenge: String,
        state: String,
    ): Uri {
        return try {
            val base = Uri.parse(authorizationEndpoint)
            val scopeValue = scopes.joinToString(separator = " ")
            base.buildUpon()
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("scope", scopeValue)
                .appendQueryParameter("code_challenge", codeChallenge)
                .appendQueryParameter("state", state)
                .appendQueryParameter("code_challenge_method", "S256")
                .build()
        } catch (e: Exception) {
            Log.e(TAG, "[ERROR] Authorization URL 생성 실패: ${e.message}")
            throw IllegalStateException("Authorization URL 생성 실패", e)
        }
    }

    companion object {
        private const val TAG = "AuthConfig"

        /**
         * 환경별 기본 설정을 생성한다.
         * - DEBUG: http://10.0.2.2:8081 (에뮬레이터에서 localhost 대체)
         * - RELEASE: https://auth.everp.co.kr
         * - clientId: everp-aos
         * - redirectUri: everp-aos://callback (Manifest에 등록됨)
         */
        fun default(): AuthConfig {
            val authBase = if (BuildConfig.DEBUG) {
                "http://10.0.2.2:8081"
            } else {
                "https://auth.everp.co.kr"
            }
            return AuthConfig(
                authorizationEndpoint = "$authBase/oauth2/authorize",
                tokenEndpoint = "$authBase/oauth2/token",
                clientId = "everp-aos",
                redirectUri = "everp-aos://callback",
                scopes = listOf("erp.user.profile", "offline_access"),
            )
        }
    }
}
