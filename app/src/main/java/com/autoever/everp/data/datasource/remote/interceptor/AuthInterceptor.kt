package com.autoever.everp.data.datasource.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JWT 토큰을 자동으로 헤더에 추가하는 Interceptor
 */
@Singleton
class AuthInterceptor @Inject constructor(
    // TODO: TokenManager 또는 DataStore를 주입받아 토큰 관리
    // private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // TODO: 실제 토큰 가져오기 로직 구현
        val token = getAccessToken()

        val newRequest = if (token != null) {
            // 토큰이 존재하면 Authorization 헤더 추가 -> Authenticated 상태 처리
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else {
            // 토큰이 없으면 원본 요청 유지 -> Authenticated 상태 처리 필요
            originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }

        return chain.proceed(newRequest)
    }

    private fun getAccessToken(): String? {
        // TODO: DataStore 또는 SharedPreferences에서 토큰 가져오기
        // return tokenManager.getAccessToken()
        return null
    }
}

