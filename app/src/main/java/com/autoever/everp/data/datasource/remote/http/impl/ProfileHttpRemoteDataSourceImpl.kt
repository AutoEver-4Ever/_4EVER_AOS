package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.ProfileRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.CustomerProfileResponseDto
import com.autoever.everp.data.datasource.remote.http.service.ProfileApi
import com.autoever.everp.data.datasource.remote.http.service.SupplierProfileResponseDto
import com.autoever.everp.domain.model.profile.Profile
import com.autoever.everp.domain.model.user.UserTypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileHttpRemoteDataSourceImpl @Inject constructor(
    private val profileApi: ProfileApi,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(
        userType: UserTypeEnum,
    ): Result<Profile> = withContext(Dispatchers.IO) {
        runCatching {
            when (userType) {
                UserTypeEnum.CUSTOMER -> {
                    val response = profileApi.getCustomerProfile()
                    response.data?.let { dto: CustomerProfileResponseDto ->
                        Profile(
                            userName = dto.customerName,
                            userEmail = dto.email,
                            userPhoneNumber = dto.phoneNumber,
                            companyName = dto.companyName,
                            businessNumber = dto.businessNumber,
                            baseAddress = dto.baseAddress,
                            detailAddress = dto.detailAddress,
                            officePhone = dto.officePhone,
                        )
                    } ?: throw Exception("Customer profile data is null")
                }

                UserTypeEnum.SUPPLIER -> {
                    val response = profileApi.getSupplierProfile()
                    response.data?.let { dto: SupplierProfileResponseDto ->
                        Profile(
                            userName = dto.supplierUserName,
                            userEmail = dto.supplierUserEmail,
                            userPhoneNumber = dto.supplierUserPhoneNumber,
                            companyName = dto.companyName,
                            businessNumber = dto.businessNumber,
                            baseAddress = dto.baseAddress,
                            detailAddress = dto.detailAddress,
                            officePhone = dto.officePhone,
                        )
                    } ?: throw Exception("Supplier profile data is null")
                }

                else -> throw Exception("Unsupported user type: $userType")
            }
        }
    }
}

