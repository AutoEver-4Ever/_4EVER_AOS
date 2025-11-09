package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile?>
    suspend fun refreshProfile(): Result<Unit>
    suspend fun getProfile(): Result<Profile>
}

