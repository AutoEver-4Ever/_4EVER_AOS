package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import com.autoever.everp.domain.model.sale.SalesOrderListParams
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
import com.autoever.everp.domain.repository.SdRepository
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

    private val _orderList = MutableStateFlow<PageResponse<SalesOrderListItem>>(
        PageResponse.empty(),
    )
    val orderList: StateFlow<PageResponse<SalesOrderListItem>> = _orderList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                sdRepository.refreshSalesOrderList(
                    SalesOrderListParams(
                        searchKeyword = _searchQuery.value,
                        searchType = if (_searchQuery.value.isNotBlank()) {
                            SalesOrderSearchTypeEnum.SALES_ORDER_NUMBER
                        } else {
                            SalesOrderSearchTypeEnum.UNKNOWN
                        },
                        page = 0,
                        size = 20,
                    ),
                ).onSuccess {
                    sdRepository.observeSalesOrderList().collect { pageResponse ->
                        _orderList.value = pageResponse
                    }
                }.onFailure { e ->
                    Timber.e(e, "주문 목록 로드 실패")
                }
            } catch (e: Exception) {
                Timber.e(e, "주문 목록 로드 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun search() {
        loadOrders()
    }
}

