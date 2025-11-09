package com.autoever.everp.di

import com.autoever.everp.BuildConfig
import com.autoever.everp.common.annotation.AuthRetrofit
import com.autoever.everp.common.annotation.NormalRetrofit
import com.autoever.everp.data.datasource.remote.http.service.AlarmApi
import com.autoever.everp.data.datasource.remote.http.service.AlarmTokenApi
import com.autoever.everp.data.datasource.remote.http.service.AuthApi
import com.autoever.everp.data.datasource.remote.http.service.FcmApi
import com.autoever.everp.data.datasource.remote.http.service.HrmApi
import com.autoever.everp.data.datasource.remote.http.service.ImApi
import com.autoever.everp.data.datasource.remote.http.service.MmApi
import com.autoever.everp.data.datasource.remote.http.service.SdApi
import com.autoever.everp.data.datasource.remote.http.service.UserApi
import com.autoever.everp.data.datasource.remote.interceptor.AuthInterceptor
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
    @AuthRetrofit
    fun provideAuthRetrofit(
        json: Json,
        loggingInterceptor: HttpLoggingInterceptor,
    ): Retrofit {
        // 인증 전용 Retrofit (Auth 서버용)
        // TODO : 공통 OkHttpClient 사용하도록 변경 검토
        // TODO : 미완성 - 타임아웃 등 설정 추가 필요
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @NormalRetrofit
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
    fun provideAlarmApiService(@NormalRetrofit retrofit: Retrofit): AlarmApi =
        retrofit.create(AlarmApi::class.java)

    @Provides
    @Singleton
    fun provideFcmTokenApiService(@NormalRetrofit retrofit: Retrofit): AlarmTokenApi =
        retrofit.create(AlarmTokenApi::class.java)

    @Provides
    @Singleton
    fun provideSdApiService(@NormalRetrofit retrofit: Retrofit): SdApi =
        retrofit.create(SdApi::class.java)

    @Provides
    @Singleton
    fun provideHrmApiService(@NormalRetrofit retrofit: Retrofit): HrmApi =
        retrofit.create(HrmApi::class.java)

    @Provides
    @Singleton
    fun provideFcmFinanceApiService(@NormalRetrofit retrofit: Retrofit): FcmApi =
        retrofit.create(FcmApi::class.java)

    @Provides
    @Singleton
    fun provideInventoryApiService(@NormalRetrofit retrofit: Retrofit): ImApi =
        retrofit.create(ImApi::class.java)

    @Provides
    @Singleton
    fun provideMaterialApiService(@NormalRetrofit retrofit: Retrofit): MmApi =
        retrofit.create(MmApi::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(@NormalRetrofit retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserAuthApiService(@AuthRetrofit retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideDashboardApiService(@NormalRetrofit retrofit: Retrofit): com.autoever.everp.data.datasource.remote.http.service.DashboardApi =
        retrofit.create(com.autoever.everp.data.datasource.remote.http.service.DashboardApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(@NormalRetrofit retrofit: Retrofit): com.autoever.everp.data.datasource.remote.http.service.ProfileApi =
        retrofit.create(com.autoever.everp.data.datasource.remote.http.service.ProfileApi::class.java)
}
