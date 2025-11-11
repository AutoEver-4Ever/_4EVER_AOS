package com.autoever.everp.data.datasource.remote.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToggleResponseDto(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String,
)
