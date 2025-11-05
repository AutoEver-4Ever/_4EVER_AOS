package com.autoever.everp.data.datasource.remote.interceptor

import com.autoever.everp.common.annotation.ApplicationScope
import com.autoever.everp.data.datasource.local.AuthLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * JWT 토큰을 자동으로 헤더에 추가하는 Interceptor
 * DataStore의 Flow를 구독하여 토큰 변경을 실시간으로 반영합니다.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : Interceptor {

    // Flow에서 받은 토큰을 동기적으로 접근 가능한 변수에 저장
    private val currentToken = AtomicReference<String?>()

    init {
        // Flow를 구독하여 토큰 변경을 실시간으로 반영
        authLocalDataSource.accessTokenFlow
            .onEach { token ->
                currentToken.set(token)
                Timber.tag(TAG).d("Access token updated: ${if (token != null) "present (length: ${token.length})" else "null"}")
            }
            .launchIn(applicationScope)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Flow에서 구독한 현재 토큰 값 사용
        val token = currentToken.get()

        val newRequest = if (token != null && token.isNotBlank()) {
            // 토큰이 존재하면 Authorization 헤더 추가
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } else {
            // 토큰이 없으면 원본 요청 유지 (Content-Type만 추가)
            originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }

        return chain.proceed(newRequest)
    }

    private companion object {
        const val TAG = "AuthInterceptor"
    }
}

