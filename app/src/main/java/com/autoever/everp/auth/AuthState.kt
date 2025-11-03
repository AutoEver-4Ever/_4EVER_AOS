package com.autoever.everp.auth

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val accessToken: String) : AuthState()
}

