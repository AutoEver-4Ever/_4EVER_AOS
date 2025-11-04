package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.SupplierDetailResponseDto
import com.autoever.everp.domain.model.supplier.SupplierDetail

/**
 * Supplier DTO to Domain Model Mapper
 */
object SupplierMapper {

    fun toDomain(dto: SupplierDetailResponseDto): SupplierDetail {

        val manager: SupplierDetail.SupplierManager = SupplierDetail.SupplierManager(
            name = dto.managerInfo.managerName,
            phone = dto.managerInfo.managerPhone,
            email = dto.managerInfo.managerEmail,
        )

        return SupplierDetail(
            id = dto.supplierInfo.supplierId,
            name = dto.supplierInfo.supplierName,
            number = dto.supplierInfo.supplierNumber,
            email = dto.supplierInfo.supplierEmail,
            phone = dto.supplierInfo.supplierPhone,
            baseAddress = dto.supplierInfo.supplierBaseAddress,
            detailAddress = dto.supplierInfo.supplierDetailAddress,
            status = dto.supplierInfo.supplierStatus,
            category = dto.supplierInfo.category,
            deliveryLeadTime = dto.supplierInfo.deliveryLeadTime,
            manager = manager,
        )
    }
}

