package com.autoever.everp.data.datasource.remote.dto.request

import com.autoever.everp.data.datasource.remote.dto.common.QuotationItemDto
import com.autoever.everp.utils.datetime.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class QuotationCreateRequestDto(
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("dueDate")
    val dueDate: LocalDate,
    @SerialName("items")
    val items: List<QuotationItemDto>,
    @SerialName("note")
    val note: String,
)
