package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.UserInfoResponseDto
import com.autoever.everp.domain.model.user.UserInfo

/**
 * User DTO to Domain Model Mapper
 */
object UserMapper {
    /**
     * UserInfoResponseDto를 UserInfo Domain Model로 변환
     */
    fun toDomain(dto: UserInfoResponseDto): UserInfo {
        return UserInfo(
            userId = dto.userId,
            userName = dto.userName,
            email = dto.email,
            userType = dto.userType,
            userRole = dto.userRole,
        )
    }
}

