package com.autoever.everp.data.repository

import com.autoever.everp.data.datasource.local.UserLocalDataSource
import com.autoever.everp.data.datasource.remote.UserRemoteDataSource
import com.autoever.everp.data.datasource.remote.mapper.UserMapper
import com.autoever.everp.domain.model.user.UserInfo
import com.autoever.everp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 사용자 Repository 구현체
 * Remote → Local → Flow 패턴
 */
class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override fun observeUserInfo(): Flow<UserInfo?> =
        userLocalDataSource.observeUserInfo()

    override suspend fun refreshUserInfo(): Result<Unit> {
        return getUserInfo().map { userInfo ->
            userLocalDataSource.setUserInfo(userInfo)
        }
    }

    override suspend fun getUserInfo(): Result<UserInfo> {
        return userRemoteDataSource.getUserInfo()
            .map { UserMapper.toDomain(it) }
    }

    override suspend fun logout() {
        userLocalDataSource.clearUserInfo()
    }
}
