package com.autoever.everp.data.datasource.local

import com.autoever.everp.domain.model.user.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * 사용자 정보 로컬 데이터소스 인터페이스
 */
interface UserLocalDataSource {
    /**
     * 사용자 정보 관찰
     */
    fun observeUserInfo(): Flow<UserInfo?>

    /**
     * 사용자 정보 저장
     */
    suspend fun setUserInfo(userInfo: UserInfo)

    /**
     * 사용자 정보 삭제 (로그아웃)
     */
    suspend fun clearUserInfo()
}
