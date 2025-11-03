package com.autoever.everp.auth

data class TokenResponse(
    val access_token: String,
    val refresh_token: String? = null,
    val token_type: String? = null,
    val expires_in: Long? = null,
    val id_token: String? = null,
)

