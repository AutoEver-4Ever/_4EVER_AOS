package com.autoever.everp.data.datasource.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 페이징 응답 공통 DTO
 */
@Serializable
data class PageResponse<T>(
    @SerialName("content")
    val content: List<T>,
    @SerialName("pageInfo")
    val page: PageDto,
    @SerialName("total")
    val total: Int,
) {
    companion object {
        fun <T> empty(): PageResponse<T> = PageResponse(
            content = emptyList(),
            page = PageDto(
                number = 0,
                size = 0,
                totalElements = 0,
                totalPages = 0,
                hasNext = false,
            ),
            total = 0,
        )
    }
}

@Serializable
data class PageDto(
    @SerialName("number")
    val number: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("totalElements")
    val totalElements: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

