package com.autoever.everp.data.datasource.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuotationItemDto(
    @SerialName("itemId")
    val id: Long,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("unitPrice")
    val unitPrice: Int,
)
