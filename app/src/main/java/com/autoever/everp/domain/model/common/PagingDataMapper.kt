package com.autoever.everp.domain.model.common

import com.autoever.everp.data.datasource.remote.dto.common.PageDto
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse

/**
 * PageResponse(DTO) -> PageData(Domain) 변환 매퍼
 */

/**
 * PageResponse를 PagingData 변환
 * @param transform content의 각 아이템을 Domain 모델로 변환하는 함수
 */
fun <T, R> PageResponse<T>.toDomain(transform: (T) -> R): PagingData<R> {
    return PagingData(
        items = content.map(transform),
        totalItems = page.totalElements,
        totalPages = page.totalPages,
        currentPage = page.number,
        size = page.size,
        hasNext = page.hasNext,
    )
}

/**
 * PageResponse를 PagingData 변환 (content가 이미 Domain 모델인 경우)
 */
fun <T> PageResponse<T>.toDomain(): PagingData<T> {
    return PagingData(
        items = content,
        totalItems = page.totalElements,
        totalPages = page.totalPages,
        currentPage = page.number,
        size = page.size,
        hasNext = page.hasNext,
    )
}

/**
 * 빈 PagingData 생성
 */
fun <T> emptyPageData(): PagingData<T> = PagingData.empty()

