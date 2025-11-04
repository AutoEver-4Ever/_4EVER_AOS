package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import com.autoever.everp.domain.model.user.UserRoleEnum
import com.autoever.everp.domain.model.user.UserTypeEnum
import kotlinx.serialization.SerialName
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
        private const val BASE_URL = "user"
    }
}

@Serializable
data class UserInfoResponseDto(
    @SerialName("userId")
    val userId: String,
    @SerialName("userName")
    val userName: String,
    @SerialName("loginEmail")
    val email: String,
    @SerialName("userType")
    val userType: UserTypeEnum, // INTERNAL, CUSTOMER, SUPPLIER
    @SerialName("userRole")
    val userRole: UserRoleEnum,
)

