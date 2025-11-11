package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.sale.SalesOrderDetail
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalesOrderDetailViewModel @Inject constructor(
    private val sdRepository: SdRepository,
) : ViewModel() {

    private val _salesOrderDetail = MutableStateFlow<SalesOrderDetail?>(null)
    val salesOrderDetail: StateFlow<SalesOrderDetail?> = _salesOrderDetail.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    fun loadSalesOrderDetail(salesOrderId: String) {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            sdRepository.refreshSalesOrderDetail(salesOrderId)
                .onSuccess {
                    sdRepository.getSalesOrderDetail(salesOrderId)
                        .onSuccess { detail ->
                            _salesOrderDetail.value = detail
                            _uiState.value = UiResult.Success(Unit)
                        }
                        .onFailure { e ->
                            _uiState.value = UiResult.Error(e as Exception)
                        }
                }
                .onFailure { e ->
                    _uiState.value = UiResult.Error(e as Exception)
                }
        }
    }

    fun retry(salesOrderId: String) {
        loadSalesOrderDetail(salesOrderId)
    }
}

