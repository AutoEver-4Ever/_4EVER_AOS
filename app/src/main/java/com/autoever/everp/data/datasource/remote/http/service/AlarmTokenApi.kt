package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * FCM 토큰 관련 API Service
 * Base URL: /alarm/fcm-tokens
 */
interface AlarmTokenApi {

    /**
     * FCM 토큰 등록
     */
    @POST("$BASE_URL/register")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRegisterRequestDto,
    ): ApiResponse<Unit>

    companion object {
        private const val BASE_URL = "alarm/fcm-tokens"
    }
}

@Serializable
data class FcmTokenRegisterRequestDto(
    @SerialName("token")
    val token: String,
    @SerialName("deviceId")
    val deviceId: String,
    @SerialName("deviceType")
    val deviceType: String = "ANDROID",
)

