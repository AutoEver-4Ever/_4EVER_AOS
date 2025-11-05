package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.InvoiceDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.InvoiceListItemDto
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.model.invoice.InvoiceListItem

/**
 * Invoice DTO to Domain Model Mapper
 */
object InvoiceMapper {

    fun toDomain(dto: InvoiceListItemDto): InvoiceListItem {

        val connection: InvoiceListItem.InvoiceListItemConnection = InvoiceListItem.InvoiceListItemConnection(
            id = dto.connection.connectionId,
            number = dto.connection.connectionNumber,
            name = dto.connection.connectionName,
        )

        val reference: InvoiceListItem.InvoiceListItemReference = InvoiceListItem.InvoiceListItemReference(
            id = dto.reference.referenceId,
            number = dto.reference.referenceNumber,
        )



        return InvoiceListItem(
            id = dto.invoiceId,
            number = dto.invoiceNumber,
            connection = connection,
            totalAmount = dto.totalAmount,
            dueDate = dto.dueDate,
            status = dto.statusCode,
            reference = reference,
        )
    }

    fun toDetailDomain(dto: InvoiceDetailResponseDto): InvoiceDetail {

        val items: List<InvoiceDetail.InvoiceDetailItem> = dto.items.map {
            InvoiceDetail.InvoiceDetailItem(
                id = it.itemId,
                name = it.itemName,
                quantity = it.quantity,
                unitOfMaterialName = it.unitOfMaterialName,
                unitPrice = it.unitPrice,
                totalPrice = it.totalPrice,
            )
        }

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
            items = items,
        )
    }

    fun toDomainList(dtoList: List<InvoiceListItemDto>): List<InvoiceListItem> {
        return dtoList.map { toDomain(it) }
    }
}

