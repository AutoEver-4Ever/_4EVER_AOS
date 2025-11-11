package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.quotation.QuotationDetail
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotationDetailViewModel @Inject constructor(
    private val sdRepository: SdRepository,
) : ViewModel() {

    private val _quotationDetail = MutableStateFlow<QuotationDetail?>(null)
    val quotationDetail: StateFlow<QuotationDetail?> = _quotationDetail.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    fun loadQuotationDetail(quotationId: String) {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            sdRepository.refreshQuotationDetail(quotationId)
                .onSuccess {
                    sdRepository.getQuotationDetail(quotationId)
                        .onSuccess { detail ->
                            _quotationDetail.value = detail
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

    fun retry(quotationId: String) {
        loadQuotationDetail(quotationId)
    }
}

