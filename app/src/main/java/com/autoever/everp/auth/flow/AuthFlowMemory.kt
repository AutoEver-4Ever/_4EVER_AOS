package com.autoever.everp.auth.flow

/**
 * 인가 플로우 중 임시 데이터를 메모리에 보관.
 * - 앱 프로세스 내 한시적 저장용으로만 사용.
 */
object AuthFlowMemory {
    var pkce: com.autoever.everp.auth.pkce.PKCEPair? = null
    var state: String? = null
    var config: com.autoever.everp.auth.config.AuthConfig? = null

    fun clear() {
        pkce = null
        state = null
        config = null
    }
}
