package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.ProfileLocalDataSource
import com.autoever.everp.data.datasource.remote.ProfileRemoteDataSource
import com.autoever.everp.domain.model.profile.Profile
import com.autoever.everp.domain.model.user.UserTypeEnum
import com.autoever.everp.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    override fun observeProfile(): Flow<Profile?> =
        profileLocalDataSource.observeProfile()

    override suspend fun refreshProfile(userType: UserTypeEnum): Result<Unit> = withContext(Dispatchers.Default) {
        getProfile(userType).map { profile ->
            profileLocalDataSource.setProfile(profile)
        }
    }

    override suspend fun getProfile(userType: UserTypeEnum): Result<Profile> = withContext(Dispatchers.Default) {
        profileRemoteDataSource.getProfile(userType)
    }
}

