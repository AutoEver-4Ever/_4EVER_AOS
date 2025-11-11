package com.autoever.everp.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.domain.model.inventory.InventoryItemToggle
import com.autoever.everp.domain.model.quotation.QuotationCreateRequest
import com.autoever.everp.domain.repository.ImRepository
import com.autoever.everp.domain.repository.SdRepository
import com.autoever.everp.utils.state.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuotationCreateViewModel @Inject constructor(
    private val imRepository: ImRepository,
    private val sdRepository: SdRepository,
) : ViewModel() {

    private val _items = MutableStateFlow<List<InventoryItemToggle>>(emptyList())
    val items: StateFlow<List<InventoryItemToggle>> = _items.asStateFlow()

    private val _uiState = MutableStateFlow<UiResult<Unit>>(UiResult.Loading)
    val uiState: StateFlow<UiResult<Unit>> = _uiState.asStateFlow()

    // 폼 상태
    val dueDate = MutableStateFlow<LocalDate?>(null)
    val note = MutableStateFlow("")

    // 선택된 품목: itemId -> SelectedItem
    data class SelectedItem(
        val item: InventoryItemToggle,
        val quantity: Int,
        val unitPrice: Long,
    ) {
        val totalPrice: Long get() = quantity * unitPrice
    }

    private val _selected = MutableStateFlow<Map<String, SelectedItem>>(emptyMap())
    val selected: StateFlow<Map<String, SelectedItem>> = _selected.asStateFlow()

    // 선택된 항목들의 총 금액
    val totalAmount: Long
        get() = _selected.value.values.sumOf { it.totalPrice }

    init {
        // Flow에서 items 업데이트를 먼저 구독
        imRepository.observeItemToggleList()
            .onEach { itemList ->
                _items.value = itemList
                // 데이터가 로드되면 UI 상태 업데이트
                if (itemList.isNotEmpty() && _uiState.value is UiResult.Loading) {
                    _uiState.value = UiResult.Success(Unit)
                }
            }
            .launchIn(viewModelScope)
        
        // 초기 데이터 로드
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = UiResult.Loading
            imRepository.refreshItemToggleList()
                .onSuccess {
                    // refresh 성공 후 observeItemToggleList()를 통해 자동으로 업데이트됨
                    // 하지만 데이터가 없을 수도 있으므로 확인
                    val currentItems = _items.value
                    if (currentItems.isEmpty()) {
                        // 직접 조회해서 확인
                        imRepository.getItemToggleList()
                            .onSuccess { items ->
                                _items.value = items
                                _uiState.value = UiResult.Success(Unit)
                            }
                            .onFailure { e ->
                                _uiState.value = UiResult.Error(e as Exception)
                            }
                    } else {
                        _uiState.value = UiResult.Success(Unit)
                    }
                }
                .onFailure { e ->
                    _uiState.value = UiResult.Error(e as Exception)
                }
        }
    }

    fun addItem(item: InventoryItemToggle) {
        _selected.value = _selected.value.toMutableMap().apply {
            put(
                item.itemId,
                SelectedItem(
                    item = item,
                    quantity = 1,
                    unitPrice = item.unitPrice,
                ),
            )
        }
    }

    fun removeItem(itemId: String) {
        _selected.value = _selected.value.toMutableMap().apply {
            remove(itemId)
        }
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        val selectedItem = _selected.value[itemId] ?: return
        if (quantity <= 0) {
            removeItem(itemId)
        } else {
            _selected.value = _selected.value.toMutableMap().apply {
                put(
                    itemId,
                    selectedItem.copy(quantity = quantity),
                )
            }
        }
    }

    fun setDueDate(date: LocalDate?) {
        dueDate.value = date
    }

    fun submit(onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (_selected.value.isEmpty()) {
                _uiState.value = UiResult.Error(Exception("품목을 선택해주세요."))
                onDone(false)
                return@launch
            }

            _uiState.value = UiResult.Loading
            val request = QuotationCreateRequest(
                dueDate = dueDate.value,
                items = _selected.value.map { (_, selectedItem) ->
                    QuotationCreateRequest.QuotationCreateRequestItem(
                        id = selectedItem.item.itemId,
                        quantity = selectedItem.quantity,
                        unitPrice = selectedItem.unitPrice,
                    )
                },
                note = note.value,
            )
            sdRepository.createQuotation(request)
                .onSuccess {
                    _uiState.value = UiResult.Success(Unit)
                    onDone(true)
                }
                .onFailure { e ->
                    _uiState.value = UiResult.Error(e as Exception)
                    onDone(false)
                }
        }
    }
}

