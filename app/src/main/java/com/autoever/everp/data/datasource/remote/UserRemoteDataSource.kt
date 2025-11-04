package com.autoever.everp.data.datasource.remote

import com.autoever.everp.data.datasource.remote.http.service.UserInfoResponseDto

/**
 * 사용자 정보 원격 데이터소스 인터페이스
 */
interface UserRemoteDataSource {
    /**
     * 현재 로그인한 사용자 정보 조회
     */
    suspend fun getUserInfo(): Result<UserInfoResponseDto>
}
