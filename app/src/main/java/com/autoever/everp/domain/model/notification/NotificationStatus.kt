package com.autoever.everp.domain.model.notification

/**
 * 알림 상태 Enum
 */
enum class NotificationStatus {
    READ,   // 읽음
    UNREAD, // 안 읽음
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            READ -> "읽음"
            UNREAD -> "안 읽음"
        }

    /**
     * API 통신용 문자열
     */
    fun toApiString(): String = this.name

    companion object {
        fun fromString(value: String): NotificationStatus? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        fun fromStringOrDefault(
            value: String,
            default: NotificationStatus = UNREAD,
        ): NotificationStatus = fromString(value) ?: default
    }
}

