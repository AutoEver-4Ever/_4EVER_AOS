package com.autoever.everp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.purchase.PurchaseOrderListItem
import com.autoever.everp.domain.model.purchase.PurchaseOrderListParams
import com.autoever.everp.domain.model.invoice.InvoiceListItem
import com.autoever.everp.domain.model.invoice.InvoiceListParams
import com.autoever.everp.domain.repository.MmRepository
import com.autoever.everp.domain.repository.FcmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SupplierHomeViewModel @Inject constructor(
    private val mmRepository: MmRepository,
    private val fcmRepository: FcmRepository,
) : ViewModel() {

    private val _recentPurchaseOrders = MutableStateFlow<List<PurchaseOrderListItem>>(emptyList())
    val recentPurchaseOrders: StateFlow<List<PurchaseOrderListItem>>
        get() = _recentPurchaseOrders.asStateFlow()

    private val _recentInvoices = MutableStateFlow<List<InvoiceListItem>>(emptyList())
    val recentInvoices: StateFlow<List<InvoiceListItem>>
        get() = _recentInvoices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    init {
        loadRecentActivities()
    }

    fun loadRecentActivities() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 최근 발주서 3개 로드
                mmRepository.refreshPurchaseOrderList(
                    PurchaseOrderListParams(
                        page = 0,
                        size = 3,
                    ),
                ).onSuccess {
                    mmRepository.getPurchaseOrderList(
                        PurchaseOrderListParams(
                            page = 0,
                            size = 3,
                        ),
                    ).onSuccess { pageResponse ->
                        _recentPurchaseOrders.value = pageResponse.content
                    }.onFailure { e ->
                        Timber.e(e, "최근 발주서 로드 실패")
                    }
                }.onFailure { e ->
                    Timber.e(e, "최근 발주서 갱신 실패")
                }

                // 최근 전표 3개 로드 (공급업체는 AR 인보이스 조회)
                fcmRepository.refreshArInvoiceList(
                    InvoiceListParams(
                        page = 0,
                        size = 3,
                    ),
                ).onSuccess {
                    fcmRepository.getArInvoiceList(
                        InvoiceListParams(
                            page = 0,
                            size = 3,
                        ),
                    ).onSuccess { pageResponse ->
                        _recentInvoices.value = pageResponse.content
                    }.onFailure { e ->
                        Timber.e(e, "최근 전표 로드 실패")
                    }
                }.onFailure { e ->
                    Timber.e(e, "최근 전표 갱신 실패")
                }
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

