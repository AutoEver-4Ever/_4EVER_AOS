package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.data.datasource.remote.dto.common.PageResponse
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import com.autoever.everp.domain.model.invoice.InvoiceListParams
import com.autoever.everp.domain.repository.FcmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CustomerVoucherViewModel @Inject constructor(
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val _invoiceList = MutableStateFlow<PageResponse<InvoiceListItem>>(
        PageResponse.empty(),
    )
    val invoiceList: StateFlow<PageResponse<InvoiceListItem>>
        get() = _invoiceList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String>
        get() = _searchQuery.asStateFlow()

    private val _selectedInvoiceIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedInvoiceIds: StateFlow<Set<String>>
        get() = _selectedInvoiceIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    init {
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 고객사는 매입전표(AP)를 조회
                fcmRepository.refreshApInvoiceList(
                    InvoiceListParams(
                        page = 0,
                        size = 20,
                    ),
                ).onSuccess {
                    fcmRepository.getApInvoiceList(
                        InvoiceListParams(
                            page = 0,
                            size = 20,
                        ),
                    ).onSuccess { pageResponse ->
                        _invoiceList.value = pageResponse
                    }.onFailure { e ->
                        Timber.e(e, "매입전표 목록 조회 실패")
                    }
                }.onFailure { e ->
                    Timber.e(e, "매입전표 목록 로드 실패")
                }
            } catch (e: Exception) {
                Timber.e(e, "매입전표 목록 로드 실패")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun search() {
        loadInvoices()
    }

    fun toggleInvoiceSelection(invoiceId: String) {
        _selectedInvoiceIds.value = if (_selectedInvoiceIds.value.contains(invoiceId)) {
            _selectedInvoiceIds.value - invoiceId
        } else {
            _selectedInvoiceIds.value + invoiceId
        }
    }

    fun selectAll() {
        _selectedInvoiceIds.value = _invoiceList.value.content.map { it.id }.toSet()
    }

    fun clearSelection() {
        _selectedInvoiceIds.value = emptySet()
    }
}

