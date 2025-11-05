package com.autoever.everp.data.datasource.local.impl

import com.autoever.everp.data.datasource.local.UserLocalDataSource
import com.autoever.everp.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSourceImpl @Inject constructor() : UserLocalDataSource {
    private val userInfoFlow = MutableStateFlow<UserInfo?>(null)

    override fun observeUserInfo(): Flow<UserInfo?> = userInfoFlow.asStateFlow()

    override suspend fun setUserInfo(userInfo: UserInfo) {
        userInfoFlow.value = userInfo
    }

    override suspend fun clearUserInfo() {
        userInfoFlow.value = null
    }
}
