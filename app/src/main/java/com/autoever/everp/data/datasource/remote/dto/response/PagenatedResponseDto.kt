package com.autoever.everp.data.datasource.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagenatedResponseDto<T>(
    @SerialName("items")
    val content: List<T>,
    @SerialName("page")
    val pageable: PageableDto,
)

@Serializable
data class PageableDto(
    @SerialName("totalElements")
    val totalElements: Long,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("hasNext")
    val last: Boolean,
    @SerialName("size")
    val size: Int,
    @SerialName("number")
    val number: Int,
)
