package com.autoever.everp.domain.model.profile

data class Profile(
    val userName: String,
    val userEmail: String,
    val userPhoneNumber: String,
    val companyName: String,
    val businessNumber: String,
    val baseAddress: String,
    val detailAddress: String,
    val officePhone: String,
) {
    val fullAddress: String
        get() = if (detailAddress.isNotBlank()) {
            "$baseAddress $detailAddress"
        } else {
            baseAddress
        }
}

