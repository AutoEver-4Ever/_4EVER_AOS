package com.autoever.everp.auth.endpoint

/**
 * 인증/인가 및 사용자 정보 관련 엔드포인트를 한 곳에 모은 상수 집합.
 * - 인가 코드 요청: https://auth.everp.co.kr/oauth2/authorize
 * - 토큰 교환: https://auth.everp.co.kr/oauth2/token
 * - 로그아웃: https://auth.everp.co.kr/logout
 * - 사용자 정보: https://everp.co.kr/api/user/info
 */
object AuthEndpoint {
    // 인증 서버 (Authorization Server)
    const val AUTH_BASE: String = "https://auth.everp.co.kr"
    const val LOCAL_BASE: String = "http://10.0.2.2:8081"

    // 앱 메인 도메인 (게이트웨이 기반 API 호출용)
    const val EVERP_BASE: String = "https://everp.co.kr"

    // OAuth2 인가 코드 (PKCE 적용)
    const val AUTHORIZE: String = "$AUTH_BASE/oauth2/authorize"
    const val TOKEN: String = "$AUTH_BASE/oauth2/token"
    const val LOGOUT: String = "$AUTH_BASE/logout"

    // 사용자 정보 (게이트웨이 -> `/api` 기본 경로)
    const val USER_INFO: String = "https://api.everp.co.kr/api/user/info"
}
