package com.autoever.everp.data.datasource.remote.dto.response

import com.autoever.everp.utils.datetime.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class QuotationListItem(
    @SerialName("quotationId")
    val quotationId: Long,
    @SerialName("quotationCode")
    val quotationCode: String,
    @SerialName("customerName")
    val customerName: String,
    @SerialName("ownerName")
    val ownerName: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("quotationDate")
    val quotationDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("totalAmount")
    val totalAmount: Int,
    @SerialName("statusCode")
    val statusCode: String,
    @SerialName("actions")
    val actions: List<String>,
)
