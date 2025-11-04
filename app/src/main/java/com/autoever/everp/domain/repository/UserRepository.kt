package com.autoever.everp.domain.repository

import com.autoever.everp.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 Repository 인터페이스
 */
interface UserRepository {
    /**
     * 로컬 캐시된 사용자 정보 관찰
     */
    fun observeUserInfo(): Flow<UserInfo?>

    /**
     * 원격에서 사용자 정보 가져와 로컬 갱신
     */
    suspend fun refreshUserInfo(): Result<Unit>

    /**
     * 사용자 정보 조회 (Remote만)
     */
    suspend fun getUserInfo(): Result<UserInfo>

    /**
     * 로그아웃 (로컬 정보 삭제)
     */
    suspend fun logout()
}
