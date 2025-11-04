package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderDetailItemDto
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.PurchaseOrderListItemDto
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem

/**
 * PurchaseOrder DTO to Domain Model Mapper
 */
object PurchaseOrderMapper {

    /**
     * PurchaseOrderListItemDto를 Domain Model로 변환
     */
    fun toDomain(dto: PurchaseOrderListItemDto): PurchaseOrderListItem {
        return PurchaseOrderListItem(
            id = dto.purchaseOrderId,
            number = dto.purchaseOrderNumber,
            supplierName = dto.supplierName,
            itemsSummary = dto.itemsSummary,
            orderDate = dto.orderDate,
            dueDate = dto.dueDate,
            totalAmount = dto.totalAmount,
            status = dto.statusCode,
        )
    }

    /**
     * PurchaseOrderDetailResponseDto를 Domain Model로 변환
     */
    fun toDetailDomain(dto: PurchaseOrderDetailResponseDto): PurchaseOrderDetail {

        val supplier = PurchaseOrderDetail.PurchaseOrderDetailSupplier(
            id = dto.supplierId,
            number = dto.supplierNumber,
            name = dto.supplierName,
            managerPhone = dto.managerPhone,
            managerEmail = dto.managerEmail,
        )

        val items = dto.items.map { toItemDomain(it) }

        return PurchaseOrderDetail(
            id = dto.purchaseOrderId,
            number = dto.purchaseOrderNumber,
            status = dto.statusCode,
            orderDate = dto.orderDate,
            dueDate = dto.dueDate,
            supplier = supplier,
            items = dto.items.map { toItemDomain(it) },
            totalAmount = dto.totalAmount,
            note = dto.note ?: "",
        )
    }

    /**
     * PurchaseOrderDetailItemDto를 Domain Model로 변환
     */
    private fun toItemDomain(dto: PurchaseOrderDetailItemDto): PurchaseOrderDetail.PurchaseOrderDetailItem {
        return PurchaseOrderDetail.PurchaseOrderDetailItem(
            id = dto.itemId,
            name = dto.itemName,
            quantity = dto.quantity,
            uomName = dto.uomName,
            unitPrice = dto.unitPrice,
            totalPrice = dto.totalPrice,
        )
    }

    /**
     * List 변환
     */
    fun toDomainList(dtoList: List<PurchaseOrderListItemDto>): List<PurchaseOrderListItem> {
        return dtoList.map { toDomain(it) }
    }
}

