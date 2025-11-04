package com.autoever.everp.auth.session

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data class Authenticated(val accessToken: String) : AuthState()
}
