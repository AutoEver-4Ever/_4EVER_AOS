package com.autoever.everp.data.datasource.remote

import com.autoever.everp.domain.model.profile.Profile

interface ProfileRemoteDataSource {
    suspend fun getProfile(): Result<Profile>
}

