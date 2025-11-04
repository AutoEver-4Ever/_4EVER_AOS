package com.autoever.everp.auth

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent

/**
 * Chrome Custom Tabs 기반 인가 플로우 실행 유틸.
 */
object AuthCct {
    fun start(context: Context) {
        val config = AuthConfig.default()
        val pkce = PKCEGenerator.generatePair()
        val state = StateGenerator.makeState()

        AuthFlowMemory.config = config
        AuthFlowMemory.pkce = pkce
        AuthFlowMemory.state = state

        val uri = config.buildAuthorizationUri(
            codeChallenge = pkce.codeChallenge,
            state = state,
        )

        val cct = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        cct.launchUrl(context, uri)
    }
}

