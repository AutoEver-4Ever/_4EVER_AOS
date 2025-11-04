package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.InvoiceDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceListItemDto
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceDetailItem
import com.autoever.everp.domain.model.invoice.InvoiceListItem

/**
 * Invoice DTO to Domain Model Mapper
 */
object InvoiceMapper {

    fun toDomain(dto: InvoiceListItemDto): InvoiceListItem {
        return InvoiceListItem(
            id = dto.invoiceId,
            number = dto.invoiceNumber,
            supplierId = dto.connection.supplierId,
            supplierNumber = dto.connection.supplierNumber,
            supplierName = dto.connection.supplierName,
            totalAmount = dto.totalAmount,
            dueDate = dto.dueDate,
            status = dto.statusCode,
            referenceId = dto.reference.referenceId,
            referenceNumber = dto.reference.referenceNumber,
        )
    }

    fun toDetailDomain(dto: InvoiceDetailResponseDto): InvoiceDetail {
        return InvoiceDetail(
            id = dto.invoiceId,
            number = dto.invoiceNumber,
            type = dto.invoiceType,
            status = dto.statusCode,
            issueDate = dto.issueDate,
            dueDate = dto.dueDate,
            connectionName = dto.connectionName,
            referenceNumber = dto.referenceNumber,
            totalAmount = dto.totalAmount,
            note = dto.note,
            items = dto.items.map {
                InvoiceDetailItem(
                    itemId = it.itemId,
                    itemName = it.itemName,
                    quantity = it.quantity,
                    unitOfMaterialName = it.unitOfMaterialName,
                    unitPrice = it.unitPrice,
                    totalPrice = it.totalPrice,
                )
            },
        )
    }

    fun toDomainList(dtoList: List<InvoiceListItemDto>): List<InvoiceListItem> {
        return dtoList.map { toDomain(it) }
    }
}

