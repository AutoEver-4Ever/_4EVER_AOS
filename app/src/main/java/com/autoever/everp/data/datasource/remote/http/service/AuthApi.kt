package com.autoever.everp.data.datasource.remote.http.service

import android.net.Uri
import com.autoever.everp.BuildConfig
import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import androidx.core.net.toUri

interface AuthApi {

    @POST(TOKEN_URL)
    suspend fun exchangeAuthCodeForToken(
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Query("grant_type") grantType: String = "authorization_code",
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("code") code: String,
        @Query("code_verifier") codeVerifier: String,
    ): TokenResponseDto

    @POST(LOGOUT_URL)
    suspend fun logout(
        @Header("Authorization") accessToken: String, // Bearer 포함하여 전달
    ): ApiResponse<LogoutResponseDto>


    fun getAuthorizationUrl(): String = "$AUTH_BASE_URL$AUTHORIZATION_URL"

    companion object {
        private const val AUTH_BASE_URL = BuildConfig.AUTH_BASE_URL
        private const val AUTHORIZATION_URL = "oauth2/authorize"
        private const val TOKEN_URL = "oauth2/token"
        private const val LOGOUT_URL = "logout"

        fun generateAuthorizationUrl(
            responseType: String = "code",
            clientId: String = "everp-aos",
            redirectUri: String = "everp-aos://callback",
            scope: String = "openid profile email",
            state: String,
            codeChallenge: String,
            codeChallengeMethod: String = "S256",
        ): String {
            return "$AUTH_BASE_URL$AUTHORIZATION_URL".toUri()
                .buildUpon() // Uri.Builder 사용
                .appendQueryParameter("response_type", responseType)
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("scope", scope)
                .appendQueryParameter("state", state)
                .appendQueryParameter("code_challenge", codeChallenge)
                .appendQueryParameter("code_challenge_method", codeChallengeMethod)
                .build()
                .toString()
        }
    }
}

@Serializable
data class TokenResponseDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("scope")
    val scope: String,
)

@Serializable
data class LogoutResponseDto(
    @SerialName("success")
    val success: Boolean,
)
