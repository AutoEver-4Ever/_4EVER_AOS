package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import com.autoever.everp.domain.model.sale.SalesOrderListParams
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerOrderViewModel @Inject constructor(
    private val sdRepository: SdRepository,
) : ViewModel() {

    // 로딩/에러 상태만 관리
    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>>
        get() = _uiState.asStateFlow()

    // 실제 리스트는 별도로 누적 관리
    private val _orderList = MutableStateFlow<List<SalesOrderListItem>>(emptyList())
    val orderList: StateFlow<List<SalesOrderListItem>>
        get() = _orderList.asStateFlow()

    private val _totalPages = MutableStateFlow(0)
    val totalPages: StateFlow<Int>
        get() = _totalPages.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean>
        get() = _hasMore.asStateFlow()

    private val _searchParams = MutableStateFlow(
        SalesOrderListParams(
            startDate = null,
            endDate = null,
            searchKeyword = "",
            searchType = SalesOrderSearchTypeEnum.UNKNOWN,
            statusFilter = com.autoever.everp.domain.model.sale.SalesOrderStatusEnum.UNKNOWN,
            page = 0,
            size = 20,
        ),
    )
    val searchParams: StateFlow<SalesOrderListParams>
        get() = _searchParams.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders(append: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading

            sdRepository.refreshSalesOrderList(searchParams.value)
                .onSuccess {
                    // refresh 후 observe를 통해 최신 데이터 가져오기
                    sdRepository.getSalesOrderList(searchParams.value)
                        .onSuccess { pageResponse ->
                            if (append) {
                                // 페이지네이션: 기존 리스트에 추가
                                _orderList.value = _orderList.value + pageResponse.content
                            } else {
                                // 새로운 검색: 리스트 교체
                                _orderList.value = pageResponse.content
                            }
                            _totalPages.value = pageResponse.page.totalPages
                            _hasMore.value = !pageResponse.page.hasNext
                            _uiState.value = UiResult.Success(Unit)
                        }
                        .onFailure { e ->
                            Timber.e(e, "주문 목록 조회 실패")
                            _uiState.value = UiResult.Error(e as Exception)
                        }
                }
                .onFailure { e ->
                    Timber.e(e, "주문 목록 로드 실패")
                    _uiState.value = UiResult.Error(e as Exception)
                }
        }
    }

    fun loadNextPage() {
        if (_uiState.value is UiResult.Loading || !_hasMore.value) return

        _searchParams.value = _searchParams.value.copy(
            page = _searchParams.value.page + 1,
        )
        loadOrders(append = true)
    }

    fun updateSearchQuery(
        query: String,
        queryType: SalesOrderSearchTypeEnum = SalesOrderSearchTypeEnum.UNKNOWN,
    ) {
        _searchParams.value = _searchParams.value.copy(
            searchKeyword = query,
            searchType = queryType,
            page = 0, // 검색 시 페이지 초기화
        )
    }

    fun search() {
        loadOrders(append = false) // 새로운 검색
    }

    fun retry() {
        loadOrders(append = false)
    }

    fun refresh() {
        _searchParams.value = _searchParams.value.copy(page = 0)
        _orderList.value = emptyList()
        loadOrders(append = false)
    }
}

