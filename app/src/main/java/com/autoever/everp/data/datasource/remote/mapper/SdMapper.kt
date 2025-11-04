package com.autoever.everp.data.datasource.remote.mapper

import com.autoever.everp.data.datasource.remote.http.service.QuotationListItemDto
import com.autoever.everp.data.datasource.remote.http.service.CustomerDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationCreateRequestDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationCreateRequestItemDto
import com.autoever.everp.data.datasource.remote.http.service.QuotationDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderDetailResponseDto
import com.autoever.everp.data.datasource.remote.http.service.SalesOrderListItemDto
import com.autoever.everp.domain.model.customer.CustomerDetail
import com.autoever.everp.domain.model.quotation.QuotationCreateRequest
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.model.sale.SalesOrderListItem

/**
 * SD(영업 관리) DTO to Domain Model Mapper
 */
object SdMapper {

    // ========== 견적서 ==========
    fun quotationListItemToDomain(dto: QuotationListItemDto): QuotationListItem {
        val customer = QuotationListItem.QuotationListItemCustomer(
            name = dto.customerName,
        )

        val product = QuotationListItem.QuotationListItemProduct(
            productId = dto.productId,
            quantity = dto.quantity,
            uomName = dto.uomName,
        )

        return QuotationListItem(
            id = dto.quotationId.toString(),
            number = dto.quotationNumber,
            customer = customer,
            status = dto.statusCode,
            dueDate = dto.dueDate,
            product = product,
        )
    }

    fun quotationDetailToDomain(dto: QuotationDetailResponseDto): QuotationDetail {
        val customer = QuotationDetail.QuotationDetailCustomer(
            name = dto.customerName,
            ceoName = dto.ceoName,
        )

        val items = dto.items.map {
            QuotationDetail.QuotationDetailItem(
                id = it.itemId,
                name = it.itemName,
                quantity = it.quantity,
                uomName = it.uomName,
                unitPrice = it.unitPrice,
                totalPrice = it.totalPrice,
            )
        }

        return QuotationDetail(
            id = dto.quotationId,
            number = dto.quotationNumber,
            issueDate = dto.quotationDate,
            dueDate = dto.dueDate,
            status = dto.statusCode,
            totalAmount = dto.totalAmount,
            customer = customer,
            items = items,
        )
    }

    fun quotationToCreateRequest(quotation: QuotationCreateRequest): QuotationCreateRequestDto {
        return QuotationCreateRequestDto(
            dueDate = quotation.dueDate,
            items = quotation.items.map {
                QuotationCreateRequestItemDto(
                    itemId = it.id,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                )
            },
            note = quotation.note,
        )
    }

    // ========== 고객사 ==========
    fun customerDetailToDomain(dto: CustomerDetailResponseDto): CustomerDetail {
        return CustomerDetail(
            customerId = dto.customerId,
            customerNumber = dto.customerNumber,
            customerName = dto.customerName,
            ceoName = dto.ceoName,
            businessNumber = dto.businessNumber,
            customerStatusCode = dto.customerStatusCode,
            contactPhone = dto.contactPhone,
            contactEmail = dto.contactEmail,
            address = dto.address,
            detailAddress = dto.detailAddress,
            managerName = dto.manager.managerName,
            managerPhone = dto.manager.managerPhone,
            managerEmail = dto.manager.managerEmail,
            totalOrders = dto.totalOrders,
            totalTransactionAmount = dto.totalTransactionAmount,
            note = dto.note,
        )
    }

    // ========== 주문서 ==========
    fun salesOrderListToDomain(dto: SalesOrderListItemDto): SalesOrderListItem {
        return SalesOrderListItem(
            salesOrderId = dto.salesOrderId,
            salesOrderNumber = dto.salesOrderNumber,
            customerName = dto.customerName,
            managerName = dto.customerManager.managerName,
            managerPhone = dto.customerManager.managerPhone,
            managerEmail = dto.customerManager.managerEmail,
            orderDate = dto.orderDate,
            dueDate = dto.dueDate,
            totalAmount = dto.totalAmount,
            statusCode = dto.statusCode,
        )
    }

    fun salesOrderDetailToDomain(dto: SalesOrderDetailResponseDto): SalesOrderDetail {
        return SalesOrderDetail(
            salesOrderId = dto.order.salesOrderId,
            salesOrderNumber = dto.order.salesOrderNumber,
            orderDate = dto.order.orderDate,
            dueDate = dto.order.dueDate,
            statusCode = dto.order.statusCode,
            totalAmount = dto.order.totalAmount,
            customerId = dto.customer.customerId,
            customerName = dto.customer.customerName,
            baseAddress = dto.customer.baseAddress,
            detailAddress = dto.customer.detailAddress,
            managerName = dto.customer.manager.managerName,
            managerPhone = dto.customer.manager.managerPhone,
            managerEmail = dto.customer.manager.managerEmail,
            items = dto.items.map {
                com.autoever.everp.domain.model.sale.SalesOrderItem(
                    itemId = it.itemId,
                    itemName = it.itemName,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    totalPrice = it.totalPrice,
                )
            },
            note = dto.note,
        )
    }

    fun salesOrderListToDomainList(dtoList: List<SalesOrderListItemDto>): List<SalesOrderListItem> {
        return dtoList.map { salesOrderListToDomain(it) }
    }
}

