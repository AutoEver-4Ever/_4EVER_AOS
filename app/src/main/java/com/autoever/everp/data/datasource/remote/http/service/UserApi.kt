package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.GET

/**
 * 사용자 정보 API Service
 * Base URL: /user
 */
interface UserApi {

    /**
     * 현재 로그인한 사용자 정보 조회
     * JWT 토큰에서 사용자 정보 추출
     */
    @GET("$BASE_URL/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto>

    companion object {
        private const val BASE_URL = "/user"
    }
}

@Serializable
data class UserInfoResponseDto(
    val userId: String,
    val username: String,
    val email: String,
    val userType: String, // INTERNAL, CUSTOMER, SUPPLIER
    val roles: List<String>,
)

