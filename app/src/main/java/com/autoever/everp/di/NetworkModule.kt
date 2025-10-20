package com.autoever.everp.di

import com.autoever.everp.BuildConfig
import com.autoever.everp.data.datasource.remote.service.SdApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class) // Application 범위에 설치
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger: HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
            }

        val headerInterceptor: Interceptor =
            Interceptor { chain ->
                val originalRequest = chain.request()
                val newRequest =
                    originalRequest
                        .newBuilder()
                        .header("Content-Type", "application/json") // 예시: JSON 타입 명시
                        .header("X-Auth-Token", "your_static_token") // 예시: 고정 인증 토큰
                        // .header("Authorization", "Bearer ${getAccessToken()}")       // 동적 토큰 (별도 로직 필요)
                        .build()
                chain.proceed(newRequest)
            }

        return OkHttpClient
            .Builder()
            .addInterceptor(logger) // 로깅 인터셉터 추가
            .addInterceptor(headerInterceptor) // 공통 헤더 추가
            .apply {
                // HTTP Timeout 설정
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        // kotlinx.serialization의 Json 객체 설정 (선택적)
        // 기본 설정을 사용해도 무방합니다.
        val json: Json =
            Json {
                isLenient = true // 유연한 JSON 파싱 허용
                ignoreUnknownKeys = true // DTO에 없는 필드 무시
                coerceInputValues = true // 기본값 허용(null 대신 기본값 사용)
            }

        val contentType = "application/json".toMediaType()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): SdApiService = retrofit.create(SdApiService::class.java)
}
