package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.ProfileLocalDataSource
import com.autoever.everp.domain.model.profile.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileLocalDataSourceImpl @Inject constructor() : ProfileLocalDataSource {
    private val profileFlow = MutableStateFlow<Profile?>(null)

    override fun observeProfile(): Flow<Profile?> = profileFlow.asStateFlow()

    override suspend fun setProfile(profile: Profile) {
        profileFlow.value = profile
    }

    override suspend fun clear() {
        profileFlow.value = null
    }
}

