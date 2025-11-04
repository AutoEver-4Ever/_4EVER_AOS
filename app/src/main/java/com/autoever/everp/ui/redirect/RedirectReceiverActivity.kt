package com.autoever.everp.ui.redirect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.autoever.everp.MainActivity
import com.autoever.everp.auth.AuthFlowMemory
import com.autoever.everp.auth.AuthService
import com.autoever.everp.auth.TokenStoreImpl
import kotlinx.coroutines.launch

/**
 * OAuth2 리다이렉트를 수신하는 투명 액티비티.
 * - CCT 리다이렉트(intent-filter) 종료 지점에서 토큰 교환 처리.
 */
class RedirectReceiverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent?.data
        if (data == null) {
            finishToMain()
            return
        }

        val config = AuthFlowMemory.config
        val pkce = AuthFlowMemory.pkce
        val expectedState = AuthFlowMemory.state

        if (config == null || pkce == null || expectedState.isNullOrEmpty()) {
            Log.e(TAG, "[ERROR] 인가 플로우 컨텍스트가 없어 토큰 교환을 진행할 수 없습니다.")
            finishToMain()
            return
        }

        val schemeOk = data.scheme == config.redirectScheme
        val hostOk = config.redirectHost?.let { data.host == it } ?: true
        val pathOk = data.path == config.redirectPath
        if (!(schemeOk && hostOk && pathOk)) {
            finishToMain()
            return
        }

        val code = data.getQueryParameter("code")
        val state = data.getQueryParameter("state")
        if (code.isNullOrEmpty() || state.isNullOrEmpty() || state != expectedState) {
            Log.e(TAG, "[ERROR] 리다이렉트 파라미터 검증 실패 (code/state)")
            finishToMain()
            return
        }

        lifecycleScope.launch {
            try {
                val token = AuthService.exchangeCodeForToken(
                    config = config,
                    code = code,
                    codeVerifier = pkce.codeVerifier,
                )
                val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                TokenStoreImpl(prefs).saveAccessToken(token.access_token)
                Log.i(TAG, "[INFO] 토큰 교환 및 저장 성공")
            } catch (e: Exception) {
                Log.e(TAG, "[ERROR] 토큰 교환 처리 실패: ${e.message}")
            } finally {
                AuthFlowMemory.clear()
                finishToMain()
            }
        }
    }

    private fun finishToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        })
        finish()
    }

    private companion object {
        const val TAG = "RedirectReceiver"
    }
}

