package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.quotation.QuotationListParams
import com.autoever.everp.domain.model.quotation.QuotationSearchTypeEnum
import com.autoever.everp.domain.model.quotation.QuotationStatusEnum
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CustomerQuotationViewModel @Inject constructor(
    private val sdRepository: SdRepository,
) : ViewModel() {

    // 로딩/에러 상태만 관리
    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>>
        get() = _uiState.asStateFlow()

    // 실제 리스트는 별도로 누적 관리
    private val _quotationList = MutableStateFlow<List<QuotationListItem>>(emptyList())
    val quotationList: StateFlow<List<QuotationListItem>>
        get() = _quotationList.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages: StateFlow<Int>
        get() = _totalPages.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean>
        get() = _hasMore.asStateFlow()

    private val _searchParams = MutableStateFlow(
        QuotationListParams(
            startDate = null,
            endDate = null,
            status = QuotationStatusEnum.UNKNOWN,
            type = QuotationSearchTypeEnum.UNKNOWN,
            search = "",
            sort = "",
            page = 0,
            size = 20,
        )
    )
    val searchParams: StateFlow<QuotationListParams>
        get() = _searchParams.asStateFlow()

    init {
        loadQuotations()
    }

    fun loadQuotations(append: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading

            sdRepository.refreshQuotationList(searchParams.value)
                .onSuccess {
                    // refresh 후 get을 통해 최신 데이터 가져오기
                    sdRepository.getQuotationList(searchParams.value)
                        .onSuccess { pageResponse ->
                            if (append) {
                                // 페이지네이션: 기존 리스트에 추가
                                _quotationList.value = _quotationList.value + pageResponse.content
                            } else {
                                // 새로운 검색: 리스트 교체
                                _quotationList.value = pageResponse.content
                            }
                            _totalPages.value = pageResponse.page.totalPages
                            _hasMore.value = !pageResponse.page.hasNext
                            _uiState.value = UiResult.Success(Unit)
                        }
                        .onFailure { e ->
                            Timber.e(e, "견적서 목록 조회 실패")
                            _uiState.value = UiResult.Error(e as Exception)
                        }
                }
                .onFailure { e ->
                    Timber.e(e, "견적서 목록 로드 실패")
                    _uiState.value = UiResult.Error(e as Exception)
                }
        }
    }

    fun loadNextPage() {
        if (_uiState.value is UiResult.Loading || !_hasMore.value) return

        _searchParams.value = _searchParams.value.copy(
            page = _searchParams.value.page + 1
        )
        loadQuotations(append = true)
    }

    fun updateSearchQuery(
        query: String,
        queryType: QuotationSearchTypeEnum = QuotationSearchTypeEnum.UNKNOWN,
    ) {
        _searchParams.value = _searchParams.value.copy(
            search = query,
            type = queryType,
            page = 0 // 검색 시 페이지 초기화
        )
    }

    fun search() {
        loadQuotations(append = false) // 새로운 검색
    }

    fun retry() {
        loadQuotations(append = false)
    }

    fun refresh() {
        _searchParams.value = _searchParams.value.copy(page = 0)
        _quotationList.value = emptyList()
        loadQuotations(append = false)
    }
}
