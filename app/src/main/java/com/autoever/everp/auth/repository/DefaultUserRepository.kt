package com.autoever.everp.auth.repository

import com.autoever.everp.auth.api.UserApi
import com.autoever.everp.auth.model.UserInfo
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val api: UserApi,
) : UserRepository {
    override suspend fun fetchUserInfo(accessToken: String): UserInfo {
        return api.getUserInfo(accessToken)
    }
}

