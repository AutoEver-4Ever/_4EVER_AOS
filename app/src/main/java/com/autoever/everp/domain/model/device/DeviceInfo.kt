package com.autoever.everp.domain.model.device

/**
 * 기기 정보 Domain Model
 */
data class DeviceInfo(
    val androidId: String,
    val manufacturer: String,
    val model: String,
    val osVersion: String,
    val sdkVersion: Int,
)
