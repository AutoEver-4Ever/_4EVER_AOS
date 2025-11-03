package org.ever.everp.aos.auth

object AuthConfig {
    // 서버 설정과 일치해야 함: AuthorizationServerConfig.java 등록값
    const val CLIENT_ID = "everp-aos"
    const val REDIRECT_URI = "everp-aos://callback"

    val SCOPES = listOf("erp.user.profile", "offline_access")
}

