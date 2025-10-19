package com.autoever.everp.data.datasource.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class PagenatedResponseDto<T>(
    val content: List<T>,
    val pageable: PageableDto,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean,
    val first: Boolean,
    val size: Int,
    val number: Int,
    val numberOfElements: Int,
    val empty: Boolean,
)

@Serializable
data class PageableDto(
    val pageNumber: Int,
    val pageSize: Int,
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean,
    val sort: SortDto,
)

@Serializable
data class SortDto(
    val sorted: Boolean,
    val unsorted: Boolean,
    val empty: Boolean,
)
