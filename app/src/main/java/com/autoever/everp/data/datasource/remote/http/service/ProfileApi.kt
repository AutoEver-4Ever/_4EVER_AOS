package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import retrofit2.http.GET

interface ProfileApi {

    @GET(BASE_URL)
    suspend fun getProfile(

    ): ApiResponse<ProfileResponseDto>

    companion object {
        private const val BASE_URL = "business/profile"
    }
}

@kotlinx.serialization.Serializable
data class ProfileResponseDto(
    @kotlinx.serialization.SerialName("businessName")
    val businessName: String,
    @kotlinx.serialization.SerialName("businessNumber")
    val businessNumber: String,
    @kotlinx.serialization.SerialName("ceoName")
    val ceoName: String,
    @kotlinx.serialization.SerialName("address")
    val address: String,
    @kotlinx.serialization.SerialName("contactNumber")
    val contactNumber: String,
)
