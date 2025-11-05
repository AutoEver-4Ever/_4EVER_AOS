package com.autoever.everp.auth.flow

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.pkce.PKCEGenerator
import com.autoever.everp.auth.pkce.StateGenerator
import timber.log.Timber

/**
 * Chrome Custom Tabs 기반 인가 플로우 실행 유틸.
 */
object AuthCct {
    fun start(context: Context) {
        Timber.tag("AuthCCT").i("[INFO] CCT 플로우 시작")
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
        Timber.tag("AuthCCT").i("[INFO] Authorize 요청 URL: $uri")

        val cct = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        cct.launchUrl(context, uri)
    }
}
