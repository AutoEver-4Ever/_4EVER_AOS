package com.autoever.everp.auth

/**
 * 인증/인가 및 사용자 정보 관련 엔드포인트를 한 곳에 모은 상수 집합.
 * - 인가 코드 요청: https://auth.everp.co.kr/oauth2/authorize
 * - 토큰 교환:      https://auth.everp.co.kr/oauth2/token
 * - 로그아웃:        https://auth.everp.co.kr/logout
 * - 사용자 정보:      https://everp.co.kr/api/user/info
 */
object AuthEndpoint {
    // Auth server (Authorization Server)
    const val AUTH_BASE: String = "https://auth.everp.co.kr"

    // App main domain for GW-backed APIs that the app should call
    const val EVERP_BASE: String = "https://everp.co.kr"

    // OAuth2 Authorization Code (with PKCE)
    const val AUTHORIZE: String = "$AUTH_BASE/oauth2/authorize"
    const val TOKEN: String = "$AUTH_BASE/oauth2/token"
    const val LOGOUT: String = "$AUTH_BASE/logout"

    // User info (Gateway -> documented as `/api` base path)
    const val USER_INFO: String = "$EVERP_BASE/api/user/info"
}

