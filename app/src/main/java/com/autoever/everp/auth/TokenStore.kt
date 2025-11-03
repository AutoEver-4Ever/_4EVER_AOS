package com.autoever.everp.auth

/**
 * 토큰 저장/조회 간단 스텁 인터페이스.
 * 이후 보안 저장소(EncryptedSharedPreferences)로 교체 예정.
 */
interface TokenStore {
    fun getAccessToken(): String?
    fun saveAccessToken(token: String)
    fun clear()
}

