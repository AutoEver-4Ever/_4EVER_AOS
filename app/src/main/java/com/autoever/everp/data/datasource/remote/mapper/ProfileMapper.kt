package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.CustomerProfileResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SupplierProfileResponseDto
import com.autoever.everp.domain.model.profile.Profile

object ProfileMapper {
    fun toDomain(dto: SupplierProfileResponseDto): Profile =
        Profile(
            userName = dto.supplierUserName,
            userEmail = dto.supplierUserEmail,
            userPhoneNumber = dto.supplierUserPhoneNumber,
            companyName = dto.companyName,
            businessNumber = dto.businessNumber,
            baseAddress = dto.baseAddress,
            detailAddress = dto.detailAddress,
            officePhone = dto.officePhone,
        )

    fun toDomain(dto: CustomerProfileResponseDto): Profile =
        Profile(
            userName = dto.customerName,
            userEmail = dto.email,
            userPhoneNumber = dto.phoneNumber,
            companyName = dto.companyName,
            businessNumber = dto.businessNumber,
            baseAddress = dto.baseAddress,
            detailAddress = dto.detailAddress,
            officePhone = dto.officePhone,
        )
}

