package com.autoever.everp.domain.model.notification

import androidx.compose.ui.graphics.Color

/**
 * 알림 상태 Enum
 */
enum class NotificationStatusEnum {
    UNKNOWN, // 알 수 없음, 기본값
    READ,   // 읽음
    UNREAD, // 안 읽음
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            READ -> "읽음"
            UNREAD -> "안 읽음"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열
     */
    fun toApiString(): String? = if (this == UNKNOWN) null else this.name

    /**
     * 상태 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 상태"
            READ -> "이미 읽은 알림"
            UNREAD -> "아직 읽지 않은 알림"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            READ -> Color(0xFFBDBDBD) // Light Grey
            UNREAD -> Color(0xFF2196F3) // Blue
        }

    /**
     * 배경 색상 (강조용)
     */
    fun toBackgroundColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFFF5F5F5) // Very Light Grey
            READ -> Color(0xFFFFFFFF) // White
            UNREAD -> Color(0xFFE3F2FD) // Light Blue
        }

    /**
     * 상태 코드 값
     */
    val code: String get() = this.name

    /**
     * 읽음 상태인지 확인
     */
    fun isRead(): Boolean = this == READ

    /**
     * 읽지 않음 상태인지 확인
     */
    fun isUnread(): Boolean = this == UNREAD

    /**
     * 알 수 없는 상태인지 확인
     */
    fun isUnknown(): Boolean = this == UNKNOWN

    /**
     * 유효한 상태인지 확인 (UNKNOWN이 아닌지)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 알림 표시가 필요한지 확인 (읽지 않음)
     */
    fun needsDisplay(): Boolean = this == UNREAD

    /**
     * 알림 뱃지 표시가 필요한지 확인
     */
    fun needsBadge(): Boolean = this == UNREAD

    /**
     * 알림 강조가 필요한지 확인
     */
    fun needsHighlight(): Boolean = this == UNREAD

    companion object {
        /**
         * 문자열을 NotificationStatusEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromStringStrict(value: String): NotificationStatusEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid NotificationStatusEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 NotificationStatusEnum으로 안전하게 변환 (null 반환)
         */
        fun fromString(value: String?): NotificationStatusEnum? =
            if (value.isNullOrBlank()) {
                null
            } else {
                try {
                    valueOf(value.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            }

        /**
         * 문자열을 NotificationStatusEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String?,
            default: NotificationStatusEnum = UNREAD,
        ): NotificationStatusEnum = fromString(value) ?: default

        /**
         * 유효한 상태 목록 (UNKNOWN 제외)
         */
        fun validStatuses(): List<NotificationStatusEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 읽지 않은 알림 목록을 필터링하기 위한 헬퍼
         */
        fun unreadStatuses(): List<NotificationStatusEnum> = listOf(UNREAD)

        /**
         * 읽은 알림 목록을 필터링하기 위한 헬퍼
         */
        fun readStatuses(): List<NotificationStatusEnum> = listOf(READ)
    }
}

