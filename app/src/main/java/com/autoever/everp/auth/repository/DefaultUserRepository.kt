package com.autoever.everp.auth.repository

import com.autoever.everp.auth.api.UserApi
import com.autoever.everp.auth.model.UserInfo
import timber.log.Timber
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val api: UserApi,
) : UserRepository {
    override suspend fun fetchUserInfo(accessToken: String): UserInfo {
        val userInfo = api.getUserInfo(accessToken)
        Timber.d("Fetched User Info: $userInfo")
        return userInfo
    }
}

