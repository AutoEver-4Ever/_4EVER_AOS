package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.ProfileResponseDto
import com.autoever.everp.domain.model.profile.Profile

object ProfileMapper {
    fun toDomain(dto: ProfileResponseDto): Profile =
        Profile(
            businessName = dto.businessName,
            businessNumber = dto.businessNumber,
            ceoName = dto.ceoName,
            address = dto.address,
            contactNumber = dto.contactNumber,
        )
}

