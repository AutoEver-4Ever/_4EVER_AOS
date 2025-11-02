package com.autoever.everp.di

import com.autoever.everp.BuildConfig
import com.autoever.everp.data.datasource.remote.interceptor.AuthInterceptor
import com.autoever.everp.data.datasource.remote.service.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 네트워크 관련 의존성 제공 Module
 * Retrofit, OkHttpClient, ApiService 등록
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            isLenient = true // 유연한 JSON 파싱 허용
            ignoreUnknownKeys = true // DTO에 없는 필드 무시
            coerceInputValues = true // 기본값 허용(null 대신 기본값 사용)
            prettyPrint = BuildConfig.DEBUG // 디버그 모드에서 보기 좋은 형식
        }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor) // JWT 인증 헤더 추가
            .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
            .apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()

        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    // ========== ApiService 제공 ==========

    @Provides
    @Singleton
    fun provideAlarmApiService(retrofit: Retrofit): AlarmApiService =
        retrofit.create(AlarmApiService::class.java)

    @Provides
    @Singleton
    fun provideFcmTokenApiService(retrofit: Retrofit): FcmTokenApiService =
        retrofit.create(FcmTokenApiService::class.java)

    @Provides
    @Singleton
    fun provideSdApiService(retrofit: Retrofit): SdApiService =
        retrofit.create(SdApiService::class.java)

    @Provides
    @Singleton
    fun provideHrmApiService(retrofit: Retrofit): HrmApiService =
        retrofit.create(HrmApiService::class.java)

    @Provides
    @Singleton
    fun provideFcmFinanceApiService(retrofit: Retrofit): FcmFinanceApiService =
        retrofit.create(FcmFinanceApiService::class.java)

    @Provides
    @Singleton
    fun provideInventoryApiService(retrofit: Retrofit): InventoryApiService =
        retrofit.create(InventoryApiService::class.java)

    @Provides
    @Singleton
    fun provideMaterialApiService(retrofit: Retrofit): MaterialApiService =
        retrofit.create(MaterialApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideDashboardApiService(retrofit: Retrofit): DashboardApiService =
        retrofit.create(DashboardApiService::class.java)
}
