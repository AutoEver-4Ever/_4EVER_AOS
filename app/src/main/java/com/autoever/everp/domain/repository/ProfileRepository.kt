package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.profile.Profile
import com.autoever.everp.domain.model.user.UserTypeEnum
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile?>
    suspend fun refreshProfile(userType: UserTypeEnum): Result<Unit>
    suspend fun getProfile(userType: UserTypeEnum): Result<Profile>
}

