package com.autoever.everp.ui.supplier

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.purchase.PurchaseOrderDetail
import com.autoever.everp.domain.repository.MmRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PurchaseOrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mmRepository: MmRepository,
) : ViewModel() {

    private val purchaseOrderId: String = savedStateHandle.get<String>(
        SupplierSubNavigationItem.PurchaseOrderDetailItem.ARG_ID,
    ) ?: ""

    private val _purchaseOrderDetail = MutableStateFlow<PurchaseOrderDetail?>(null)
    val purchaseOrderDetail: StateFlow<PurchaseOrderDetail?> = _purchaseOrderDetail.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    init {
        if (purchaseOrderId.isNotEmpty()) {
            loadPurchaseOrderDetail()
        }
    }

    fun loadPurchaseOrderDetail() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading

            mmRepository.refreshPurchaseOrderDetail(purchaseOrderId)
                .onSuccess {
                    mmRepository.getPurchaseOrderDetail(purchaseOrderId)
                        .onSuccess { detail ->
                            _purchaseOrderDetail.value = detail
                            _uiState.value = UiResult.Success(Unit)
                        }
                        .onFailure { e ->
                            Timber.e(e, "발주서 상세 조회 실패")
                            _uiState.value = UiResult.Error(e as Exception)
                        }
                }
                .onFailure { e ->
                    Timber.e(e, "발주서 상세 로드 실패")
                    _uiState.value = UiResult.Error(e as Exception)
                }
        }
    }
}

