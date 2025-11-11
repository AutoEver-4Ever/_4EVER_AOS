package com.autoever.everp.data.datasource.local

import com.autoever.everp.domain.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileLocalDataSource {
    fun observeProfile(): Flow<Profile?>
    suspend fun setProfile(profile: Profile)
    suspend fun clear()
}

