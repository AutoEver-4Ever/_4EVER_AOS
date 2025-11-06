package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.domain.model.quotation.QuotationListItem
import com.autoever.everp.domain.model.quotation.QuotationListParams
import com.autoever.everp.domain.model.sale.SalesOrderListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerHomeViewModel @Inject constructor(
    private val sdRepository: SdRepository,
) : ViewModel() {

    private val _recentQuotations = MutableStateFlow<List<QuotationListItem>>(emptyList())
    val recentQuotations: StateFlow<List<QuotationListItem>>
        get() = _recentQuotations.asStateFlow()

    private val _recentOrders = MutableStateFlow<List<SalesOrderListItem>>(emptyList())
    val recentOrders: StateFlow<List<SalesOrderListItem>> = _recentOrders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRecentActivities()
    }

    fun loadRecentActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            try {

            } catch (e: Exception) {
                Timber.e(e, "최근 활동 로드 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadRecentActivities()
    }
}

