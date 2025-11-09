package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.ProfileRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.ProfileApi
import com.autoever.everp.data.datasource.remote.http.service.ProfileResponseDto
import com.autoever.everp.domain.model.profile.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileHttpRemoteDataSourceImpl @Inject constructor(
    private val profileApi: ProfileApi,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(

    ): Result<Profile> = withContext(Dispatchers.IO) {
        runCatching {
            val response = profileApi.getProfile() //.data ?: throw Exception("Profile data is null")
            response.data?.let { dto: ProfileResponseDto ->
                Profile(
                    businessName = dto.businessName,
                    businessNumber = dto.businessNumber,
                    ceoName = dto.ceoName,
                    address = dto.address,
                    contactNumber = dto.contactNumber,
                )
            } ?: throw Exception("Profile data is null")
        }
    }

}

