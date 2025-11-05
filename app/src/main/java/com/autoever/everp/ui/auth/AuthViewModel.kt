package com.autoever.everp.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.autoever.everp.auth.config.AuthConfig
import com.autoever.everp.auth.pkce.PKCEGenerator
import com.autoever.everp.auth.pkce.PKCEPair
import com.autoever.everp.auth.pkce.StateGenerator
import timber.log.Timber

class AuthViewModel : ViewModel() {
    var requestUri: Uri? = null
        private set

    var isLoading: Boolean = false
        private set

    var errorMessage: String? = null
        private set

    private var pkce: PKCEPair? = null
    private var state: String = ""
    private var config: AuthConfig? = null

    fun start(config: AuthConfig) {
        this.config = config
        isLoading = true
        errorMessage = null

        Timber.tag(TAG).i("[INFO] 인가(Authorization) 플로우 시작")
        try {
            val pair = PKCEGenerator.generatePair()
            val st = StateGenerator.makeState()
            this.pkce = pair
            this.state = st

            val uri = config.buildAuthorizationUri(
                codeChallenge = pair.codeChallenge,
                state = st,
            )
            requestUri = uri
            isLoading = false
            Timber.tag(TAG).i("[INFO] Authorization URL 생성 완료: ${uri}")
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
            Timber.tag(TAG).e("[ERROR] 인가 URL 생성 중 오류: ${e.message}")
        }
    }

    fun handleRedirect(url: Uri, onCode: (code: String, codeVerifier: String) -> Unit) {
        val cfg = config ?: run {
            errorMessage = "인가 설정 정보 없음"
            Timber.tag(TAG).e("[ERROR] 인가 설정 정보가 없습니다.")
            return
        }

        val schemeOk = url.scheme == cfg.redirectScheme
        val hostOk = cfg.redirectHost?.let { url.host == it } ?: true
        val pathOk = url.path == cfg.redirectPath
        if (!(schemeOk && hostOk && pathOk)) {
            return
        }

        val code = url.getQueryParameter("code")
        val receivedState = url.getQueryParameter("state")
        if (code.isNullOrEmpty() || receivedState.isNullOrEmpty() || receivedState != state) {
            errorMessage = "state 또는 code 검증 실패"
            Timber.tag(TAG).e("[ERROR] state 또는 code 검증 실패 (state 불일치 혹은 누락)")
            return
        }

        val verifier = pkce?.codeVerifier
        if (verifier.isNullOrEmpty()) {
            errorMessage = "code_verifier 추출 실패"
            Timber.tag(TAG).e("[ERROR] code_verifier 추출 실패 (PKCE 초기화 누락 가능성)")
            return
        }

        Timber.tag(TAG).i("[INFO] 인가 코드 수신 완료: 토큰 교환 진행 가능")
        onCode(code, verifier)
    }

    fun reset() {
        requestUri = null
        isLoading = false
        errorMessage = null
        pkce = null
        state = ""
        config = null
        Timber.tag(TAG).i("[INFO] AuthViewModel 상태 초기화 완료")
    }

    private companion object {
        const val TAG = "AuthViewModel"
    }
}
