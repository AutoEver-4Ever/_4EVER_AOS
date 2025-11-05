package com.autoever.everp.auth.model

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val idToken: String? = null,
)
