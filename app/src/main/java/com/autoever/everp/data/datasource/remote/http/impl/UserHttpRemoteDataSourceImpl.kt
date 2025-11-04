package com.autoever.everp.data.datasource.remote.http.impl

import com.autoever.everp.data.datasource.remote.UserRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.service.UserApi
import com.autoever.everp.data.datasource.remote.http.service.UserInfoResponseDto
import timber.log.Timber
import javax.inject.Inject

/**
 * 사용자 정보 원격 데이터소스 구현체
 */
class UserHttpRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
) : UserRemoteDataSource {

    override suspend fun getUserInfo(): Result<UserInfoResponseDto> {
        return try {
            val response = userApi.getUserInfo()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "사용자 정보 조회 실패"))
            }
        } catch (e: Exception) {
            Timber.e(e, "사용자 정보 조회 실패")
            Result.failure(e)
        }
    }
}
