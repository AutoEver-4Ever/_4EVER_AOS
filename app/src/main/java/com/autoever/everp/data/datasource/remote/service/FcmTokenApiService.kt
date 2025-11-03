package com.autoever.everp.data.datasource.remote.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * FCM 토큰 관련 API Service
 * Base URL: /alarm/fcm-tokens
 */
interface FcmTokenApiService {

    /**
     * FCM 토큰 등록
     */
    @POST("alarm/fcm-tokens/register")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRegisterRequestDto,
    ): ApiResponse<Unit>
}

@kotlinx.serialization.Serializable
data class FcmTokenRegisterRequestDto(
    val token: String,
    val deviceId: String?,
    val deviceType: String, // IOS, ANDROID, WEB
)

