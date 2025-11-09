package com.autoever.everp.ui.supplier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoever.everp.data.datasource.remote.http.service.SupplierUpdateRequestDto
import com.autoever.everp.domain.model.supplier.SupplierDetail
import com.autoever.everp.domain.model.user.UserInfo
import com.autoever.everp.domain.repository.MmRepository
import com.autoever.everp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SupplierProfileEditViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mmRepository: MmRepository,
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo.asStateFlow()

    private val _supplierDetail = MutableStateFlow<SupplierDetail?>(null)
    val supplierDetail: StateFlow<SupplierDetail?> = _supplierDetail.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            userRepository.getUserInfo().onSuccess { user ->
                _userInfo.value = user
                user.userId.let { supplierId ->
                    mmRepository.getSupplierDetail(supplierId).onSuccess { detail ->
                        _supplierDetail.value = detail
                    }
                }
            }
        }
    }

    fun saveProfile(
        companyName: String,
        companyAddress: String,
        companyPhone: String,
        companyEmail: String,
        managerPhone: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val supplierId = _userInfo.value?.userId ?: return@launch

                val supplierDetail = _supplierDetail.value
                    ?: return@launch

                val (baseAddress, detailAddress) = parseAddress(companyAddress)
                val request = SupplierUpdateRequestDto(
                    supplierName = companyName,
                    supplierEmail = companyEmail,
                    supplierPhone = companyPhone,
                    supplierBaseAddress = baseAddress,
                    supplierDetailAddress = detailAddress,
                    category = supplierDetail.category,
                    statusCode = supplierDetail.status,
                    deliveryLeadTime = supplierDetail.deliveryLeadTime,
                    managerName = supplierDetail.manager.name,
                    managerPhone = managerPhone,
                    managerEmail = supplierDetail.manager.email,
                )

                mmRepository.updateSupplier(supplierId, request)
                    .onSuccess {
                        Timber.i("프로필 저장 성공")
                        onSuccess()
                    }
                    .onFailure { e ->
                        Timber.e(e, "프로필 저장 실패")
                    }
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun parseAddress(fullAddress: String): Pair<String, String?> {
        // 간단한 주소 파싱: 공백으로 구분하여 첫 번째 부분을 baseAddress, 나머지를 detailAddress로
        val parts = fullAddress.trim().split(" ", limit = 2)
        return if (parts.size == 2) {
            Pair(parts[0], parts[1].takeIf { it.isNotBlank() })
        } else {
            Pair(fullAddress, null)
        }
    }
}

