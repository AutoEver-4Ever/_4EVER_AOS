package com.autoever.everp.ui.supplier

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.invoice.InvoiceDetail
import com.autoever.everp.domain.repository.FcmRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val invoiceId: String = savedStateHandle.get<String>(
        SupplierSubNavigationItem.InvoiceDetailItem.ARG_ID,
    ) ?: ""

    private val isAp: Boolean = savedStateHandle.get<Boolean>(
        SupplierSubNavigationItem.InvoiceDetailItem.ARG_IS_AP,
    ) ?: true

    private val _invoiceDetail = MutableStateFlow<InvoiceDetail?>(null)
    val invoiceDetail: StateFlow<InvoiceDetail?> = _invoiceDetail.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    init {
        if (invoiceId.isNotEmpty()) {
            loadInvoiceDetail()
        }
    }

    fun loadInvoiceDetail() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading

            if (isAp) {
                fcmRepository.refreshApInvoiceDetail(invoiceId)
                    .onSuccess {
                        fcmRepository.getApInvoiceDetail(invoiceId)
                            .onSuccess { detail ->
                                _invoiceDetail.value = detail
                                _uiState.value = UiResult.Success(Unit)
                            }
                            .onFailure { e ->
                                Timber.e(e, "매입전표 상세 조회 실패")
                                _uiState.value = UiResult.Error(e as Exception)
                            }
                    }
                    .onFailure { e ->
                        Timber.e(e, "매입전표 상세 로드 실패")
                        _uiState.value = UiResult.Error(e as Exception)
                    }
            } else {
                fcmRepository.refreshArInvoiceDetail(invoiceId)
                    .onSuccess {
                        fcmRepository.getArInvoiceDetail(invoiceId)
                            .onSuccess { detail ->
                                _invoiceDetail.value = detail
                                _uiState.value = UiResult.Success(Unit)
                            }
                            .onFailure { e ->
                                Timber.e(e, "매출전표 상세 조회 실패")
                                _uiState.value = UiResult.Error(e as Exception)
                            }
                    }
                    .onFailure { e ->
                        Timber.e(e, "매출전표 상세 로드 실패")
                        _uiState.value = UiResult.Error(e as Exception)
                    }
            }
        }
    }
}

