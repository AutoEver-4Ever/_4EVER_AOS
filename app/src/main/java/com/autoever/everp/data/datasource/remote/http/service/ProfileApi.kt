package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface ProfileApi {

    @GET(BASE_URL)
    suspend fun getProfile(

    ): ApiResponse<ProfileResponseDto>

    companion object {
        private const val BASE_URL = "business/profile"
    }
}

@Serializable
data class ProfileResponseDto(
    @SerialName("businessName")
    val businessName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("ceoName")
    val ceoName: String,
    @SerialName("address")
    val address: String,
    @SerialName("contactNumber")
    val contactNumber: String,
)
