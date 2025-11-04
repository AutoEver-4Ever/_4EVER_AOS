package com.autoever.everp.domain.model.notification

import androidx.compose.ui.graphics.Color
import com.autoever.everp.utils.serializer.NotificationSourceEnumSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NotificationSourceEnumSerializer::class)
enum class NotificationSourceEnum {
    UNKNOWN, // 알 수 없는 소스, 기본값
    PR,  // 구매부
    SD,  // 영업부
    IM,  // 재고부
    FCM, // 재무부
    HRM, // 인사부
    PP,  // 생산부
    CUS, // Customer (고객)
    SUP, // Supplier (공급사)
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            PR -> "구매"
            SD -> "영업"
            IM -> "재고"
            FCM -> "재무"
            HRM -> "인사"
            PP -> "생산"
            CUS -> "고객"
            SUP -> "공급사"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String? = if (this == UNKNOWN) null else this.name

    /**
     * 모듈 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 소스"
            PR -> "구매 관리 모듈"
            SD -> "영업 및 유통 모듈"
            IM -> "재고 관리 모듈"
            FCM -> "재무 및 원가 관리 모듈"
            HRM -> "인사 관리 모듈"
            PP -> "생산 관리 모듈"
            CUS -> "고객 관리"
            SUP -> "공급사 관리"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey (기본값)
            PR -> Color(0xFF2196F3) // Blue
            SD -> Color(0xFF4CAF50) // Green
            IM -> Color(0xFFFF9800) // Orange
            FCM -> Color(0xFF9C27B0) // Purple
            HRM -> Color(0xFFF44336) // Red
            PP -> Color(0xFF00BCD4) // Cyan
            CUS -> Color(0xFF607D8B) // Blue Grey
            SUP -> Color(0xFF795548) // Brown
        }

    /**
     * 소스 코드 값 (서버에서 사용하는 실제 코드값)
     */
    val code: String get() = this.name

    companion object {
        /**
         * 문자열을 NotificationSourceEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): NotificationSourceEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid NotificationSourceEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 NotificationSourceEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): NotificationSourceEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 NotificationSourceEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: NotificationSourceEnum = UNKNOWN, // 알 수 없는 경우 기본값
        ): NotificationSourceEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환 (API 필터링용)
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 필터 가능한 소스 목록 (UNKNOWN, CUS, SUP 제외)
         */
        fun getFilterableSources(): List<NotificationSourceEnum> =
            entries.filter { it != UNKNOWN && it != CUS && it != SUP }
    }
}
