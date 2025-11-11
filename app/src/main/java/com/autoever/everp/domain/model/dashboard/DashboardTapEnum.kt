package com.autoever.everp.domain.model.dashboard

import androidx.compose.ui.graphics.Color
import com.autoever.everp.ui.customer.CustomerSubNavigationItem

enum class DashboardTapEnum {
    UNKNOWN, // 알 수 없음, 기본값
    PO, // 발주, Purchase Order
    AP, // 매입, Accounts Payable
    AR, // 매출, Accounts Receivable
    SO, // 주문, Sales Order
    PR, // 구매, Purchase Requisition
    ATT, // 근태, Attendance
    LV, // 휴가, Leave
    QT, // 견적, Quotation
    MES, // 생산, Manufacturing Execution System
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            PO -> "발주"
            AP -> "매입"
            AR -> "매출"
            SO -> "주문"
            PR -> "구매"
            ATT -> "근태"
            LV -> "휴가"
            QT -> "견적"
            MES -> "생산"
        }

    /**
     * 표시 이름
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String? = if (this != UNKNOWN) this.name else null

    /**
     * 탭 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 탭"
            PO -> "발주서 관리 및 조회"
            AP -> "매입 전표 관리 및 조회"
            AR -> "매출 전표 관리 및 조회"
            SO -> "주문서 관리 및 조회"
            PR -> "구매 요청서 관리 및 조회"
            ATT -> "근태 관리 및 조회"
            LV -> "휴가 신청 및 관리"
            QT -> "견적서 관리 및 조회"
            MES -> "생산 계획 및 실행 관리"
        }

    /**
     * 전체 이름 (영문)
     */
    fun fullName(): String =
        when (this) {
            UNKNOWN -> "Unknown"
            PO -> "Purchase Order"
            AP -> "Accounts Payable"
            AR -> "Accounts Receivable"
            SO -> "Sales Order"
            PR -> "Purchase Requisition"
            ATT -> "Attendance"
            LV -> "Leave"
            QT -> "Quotation"
            MES -> "Manufacturing Execution System"
        }

    /**
     * UI 색상 (Compose Color)
     * TODO: 실제 디자인 시스템 색상으로 교체
     */
    fun toColor(): Color =
        when (this) {
            UNKNOWN -> Color(0xFF9E9E9E) // Grey
            PO -> Color(0xFF9C27B0) // Purple
            AP -> Color(0xFFF44336) // Red
            AR -> Color(0xFF4CAF50) // Green
            SO -> Color(0xFF2196F3) // Blue
            PR -> Color(0xFFFF9800) // Orange
            ATT -> Color(0xFF00BCD4) // Cyan
            LV -> Color(0xFFFFEB3B) // Yellow
            QT -> Color(0xFF3F51B5) // Indigo
            MES -> Color(0xFF795548) // Brown
        }

    /**
     * 탭 코드 값
     */
    val code: String get() = this.name

    /**
     * 유효한 탭인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 구매 관련 탭인지 확인
     */
    fun isPurchaseRelated(): Boolean = this == PO || this == PR

    /**
     * 영업 관련 탭인지 확인
     */
    fun isSalesRelated(): Boolean = this == SO || this == QT || this == AR

    /**
     * 재무 관련 탭인지 확인
     */
    fun isFinanceRelated(): Boolean = this == AP || this == AR

    /**
     * 인사 관련 탭인지 확인
     */
    fun isHrRelated(): Boolean = this == ATT || this == LV

    /**
     * 생산 관련 탭인지 확인
     */
    fun isProductionRelated(): Boolean = this == MES

    /**
     * 카테고리 반환
     */
    fun getCategory(): String =
        when {
            isPurchaseRelated() -> "구매"
            isSalesRelated() -> "영업"
            isFinanceRelated() -> "재무"
            isHrRelated() -> "인사"
            isProductionRelated() -> "생산"
            else -> "기타"
        }

    /**
     * 관련 NotificationLinkEnum 반환
     */
    fun toNotificationLink(): com.autoever.everp.domain.model.notification.NotificationLinkEnum? =
        when (this) {
            QT -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.QUOTATION
            SO -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.SALES_ORDER
            AP -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.PURCHASE_INVOICE
            AR -> com.autoever.everp.domain.model.notification.NotificationLinkEnum.SALES_INVOICE
            else -> null
        }

    fun isCustomerRelated(): Boolean {
        return this == AR || this == SO || this == QT
    }

    fun isSupplierRelated(): Boolean {
        return this == AP || this == PO
    }

    companion object {
        /**
         * 문자열을 DashboardTapEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): DashboardTapEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid DashboardTapEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 DashboardTapEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): DashboardTapEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 DashboardTapEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: DashboardTapEnum = UNKNOWN,
        ): DashboardTapEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 유효한 탭 목록 (UNKNOWN 제외)
         */
        fun getValidTaps(): List<DashboardTapEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * 구매 관련 탭 목록
         */
        fun getPurchaseTaps(): List<DashboardTapEnum> =
            entries.filter { it.isPurchaseRelated() }

        /**
         * 영업 관련 탭 목록
         */
        fun getSalesTaps(): List<DashboardTapEnum> =
            entries.filter { it.isSalesRelated() }

        /**
         * 재무 관련 탭 목록
         */
        fun getFinanceTaps(): List<DashboardTapEnum> =
            entries.filter { it.isFinanceRelated() }

        /**
         * 인사 관련 탭 목록
         */
        fun getHrTaps(): List<DashboardTapEnum> =
            entries.filter { it.isHrRelated() }

        /**
         * 생산 관련 탭 목록
         */
        fun getProductionTaps(): List<DashboardTapEnum> =
            entries.filter { it.isProductionRelated() }

        /**
         * 카테고리별 탭 그룹핑
         */
        fun getTapsByCategory(): Map<String, List<DashboardTapEnum>> =
            getValidTaps().groupBy { it.getCategory() }

        fun isCustomerRelated(tap: DashboardTapEnum): Boolean {
            return tap == AR || tap == SO || tap == QT
        }

        fun isSupplierRelated(tap: DashboardTapEnum): Boolean {
            return tap == AP || tap == PO
        }
    }
}
