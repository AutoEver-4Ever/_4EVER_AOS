package com.autoever.everp.ui.customer

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
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailViewModel @Inject constructor(
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val _invoiceDetail = MutableStateFlow<InvoiceDetail?>(null)
    val invoiceDetail: StateFlow<InvoiceDetail?> = _invoiceDetail.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    private val _requestResult = MutableStateFlow<Result<Unit>?>(null)
    val requestResult: StateFlow<Result<Unit>?> = _requestResult.asStateFlow()

    fun loadInvoiceDetail(invoiceId: String, isAp: Boolean) {
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
                                _uiState.value = UiResult.Error(e as Exception)
                            }
                    }
                    .onFailure { e ->
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
                                _uiState.value = UiResult.Error(e as Exception)
                            }
                    }
                    .onFailure { e ->
                        _uiState.value = UiResult.Error(e as Exception)
                    }
            }
        }
    }

    fun retry(invoiceId: String, isAp: Boolean) {
        loadInvoiceDetail(invoiceId, isAp)
    }

    fun requestReceivable(invoiceId: String) {
        viewModelScope.launch {
            _requestResult.value = fcmRepository.requestReceivable(invoiceId)
                .onSuccess {
                    // 성공 시 상세 정보 다시 로드
                    loadInvoiceDetail(invoiceId, false) // AR 인보이스
                }
        }
    }

    fun clearRequestResult() {
        _requestResult.value = null
    }
}

