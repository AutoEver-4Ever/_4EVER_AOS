package com.autoever.everp.data.datasource.remote.http.service

import com.autoever.everp.data.datasource.remote.dto.common.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface ProfileApi {

    @GET(BASE_URL)
    suspend fun getCustomerProfile(

    ): ApiResponse<CustomerProfileResponseDto>

    @GET(BASE_URL)
    suspend fun getSupplierProfile(

    ): ApiResponse<SupplierProfileResponseDto>


    companion object {
        private const val BASE_URL = "business/profile"
    }
}

@Serializable
data class CustomerProfileResponseDto(
    @SerialName("customerName")
    val customerName: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("companyName")
    val companyName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("baseAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detailAddress: String,
    @SerialName("officePhone")
    val officePhone: String,
)

@Serializable
data class SupplierProfileResponseDto(
    @SerialName("supplierUserName")
    val supplierUserName: String,
    @SerialName("supplierUserEmail")
    val supplierUserEmail: String,
    @SerialName("supplierUserPhoneNumber")
    val supplierUserPhoneNumber: String,
    @SerialName("companyName")
    val companyName: String,
    @SerialName("businessNumber")
    val businessNumber: String,
    @SerialName("baseAddress")
    val baseAddress: String,
    @SerialName("detailAddress")
    val detailAddress: String,
    @SerialName("officePhone")
    val officePhone: String,
)
