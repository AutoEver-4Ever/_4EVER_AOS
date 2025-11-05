package com.autoever.everp.domain.model.auth

import java.time.LocalDateTime

data class AccessToken(
    val token: String,
    val expiresIn: LocalDateTime,
    val type: String = "Bearer"
)
