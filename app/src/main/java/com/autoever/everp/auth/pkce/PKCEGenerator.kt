package com.autoever.everp.auth.pkce

import android.util.Base64
import timber.log.Timber
import java.security.MessageDigest
import java.security.SecureRandom

data class PKCEPair(
    val codeVerifier: String,
    val codeChallenge: String,
)

/**
 * PKCE(code_verifier, code_challenge) 생성 유틸.
 * - code_verifier: URL-safe Base64(패딩 제거), 길이 43~128 권장(기본 64바이트 → ~86문자)
 * - code_challenge: S256(SHA-256) → URL-safe Base64(패딩 제거)
 */
object PKCEGenerator {
    private const val TAG = "PKCEGenerator"
    private const val DEFAULT_VERIFIER_BYTES = 64

    fun generatePair(verifierBytes: Int = DEFAULT_VERIFIER_BYTES): PKCEPair {
        val verifier = generateCodeVerifier(verifierBytes)
        val challenge = generateCodeChallenge(verifier)
        Timber.tag(TAG).i("[INFO] PKCE 값 생성 완료 (verifier 길이: ${verifier.length})")
        return PKCEPair(verifier, challenge)
    }

    fun generateCodeVerifier(numBytes: Int = DEFAULT_VERIFIER_BYTES): String {
        require(numBytes >= 32) { "code_verifier용 바이트 길이는 최소 32 이상이어야 합니다." }
        val random = SecureRandom()
        val bytes = ByteArray(numBytes)
        random.nextBytes(bytes)
        // URL-safe Base64, padding 제거, 줄바꿈 제거
        val b64 = Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        // Base64.URL_SAFE는 '-'와 '_'를 사용하므로 URL-safe 만족
        if (b64.length < 43 || b64.length > 128) {
            Timber.tag(TAG).i("[INFO] code_verifier 길이 조정이 필요할 수 있습니다. (현재: ${b64.length})")
        }
        return b64
    }

    fun generateCodeChallenge(verifier: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray(Charsets.UTF_8))
        // URL-safe Base64, padding 제거, 줄바꿈 제거
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
}
