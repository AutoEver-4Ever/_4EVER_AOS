package com.autoever.everp.domain.model.common

data class PagingData<T>(
    val items: List<T> = emptyList(),
    val totalItems: Int = 0,
    val totalPages: Int = 0,
    val currentPage: Int = 0,
    val size: Int = 20,
    val hasNext: Boolean = false,
) {

    val hasPrevious: Boolean
        get() = currentPage > 0

    val isFirst: Boolean
        get() = currentPage == 0

    val isLast: Boolean
        get() = !hasNext

    companion object {
        fun <T> empty(): PagingData<T> = PagingData(
            items = emptyList(),
            totalItems = 0,
            totalPages = 0,
            currentPage = 0,
            size = 20,
            hasNext = false,
        )
    }
}
