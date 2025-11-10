package com.autoever.everp.data.datasource.remote

import com.autoever.everp.domain.model.profile.Profile
import com.autoever.everp.domain.model.user.UserTypeEnum

interface ProfileRemoteDataSource {
    suspend fun getProfile(userType: UserTypeEnum): Result<Profile>
}

