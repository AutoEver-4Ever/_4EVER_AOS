package com.autoever.everp.domain.model.notification

import com.autoever.everp.utils.serializer.NotificationLinkEnumSerializer
import kotlinx.serialization.Serializable

/**
 * 참조 문서 유형을 나타내는 열거형
 * 각 유형은 다양한 비즈니스 문서 형식을 나타냅니다.
 * 예: 견적서, 청구서, 구매 요청서 등
 * PR (구매부)
 * _PURCHASE_REQUEST(구매 요청서)
 * _PURCHASE_ORDER(발주서)
 * _ETC (기타) - 화면 이동 없음
 *
 * SD (영업부)
 * _QUOTATION(견적서)
 * _SALES_ORDER(주문서)
 * _ETC(기타) - 화면 이동 없음
 *
 * IM (재고부)
 * _ETC(재고 부족, 입고, 출고, 기타) - 화면 이동 없음
 *
 * FCM (재무부)
 * _SALES_INVOICE(매출 청구서, 만기일)
 * _PURCHASE_INVOICE(매입 청구서, 만기일)
 * _ETC(기타) - 화면 이동 없음
 *
 * HRM (인사부)
 * _ETC(휴가 신청서, 휴가 승인서, 급여 명세서, 교육, 기타) - 화면 이동 없음
 *
 * PP (생산부)
 * _ESTIMATE(견적서)
 * _INSUFFICIENT_STOCK(가용 재고 부족)
 * _ETC(기타) - 화면 이동 없음
 */
@Serializable(with = NotificationLinkEnumSerializer::class)
enum class NotificationLinkEnum {
    UNKNOWN, // 알 수 없음, 기본값

    // PR (구매부)
    PURCHASE_REQUISITION,
    PURCHASE_ORDER,
    PR_ETC,

    // SD (영업부)
    QUOTATION,
    SALES_ORDER,
    SD_ETC,

    // IM (재고부)
    IM_ETC,

    // FCM (재무부)
    SALES_INVOICE,
    PURCHASE_INVOICE,
    FCM_ETC,

    // HRM (인사부)
    HRM_ETC,

    // PP (생산부)
    ESTIMATE,
    INSUFFICIENT_STOCK,
    PP_ETC,
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            // PR (구매부)
            PURCHASE_REQUISITION -> "구매 요청서"
            PURCHASE_ORDER -> "발주서"
            PR_ETC -> "구매 알림"
            // SD (영업부)
            QUOTATION -> "견적서"
            SALES_ORDER -> "주문서"
            SD_ETC -> "영업 알림"
            // IM (재고부)
            IM_ETC -> "재고 알림"
            // FCM (재무부)
            SALES_INVOICE -> "매출 청구서"
            PURCHASE_INVOICE -> "매입 청구서"
            FCM_ETC -> "재무 알림"
            // HRM (인사부)
            HRM_ETC -> "인사 알림"
            // PP (생산부)
            ESTIMATE -> "견적서"
            INSUFFICIENT_STOCK -> "재고 부족"
            PP_ETC -> "생산 알림"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 문서 유형 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 문서 유형"
            // PR (구매부)
            PURCHASE_REQUISITION -> "구매 요청서 문서"
            PURCHASE_ORDER -> "발주서 문서"
            PR_ETC -> "구매부 기타 알림"
            // SD (영업부)
            QUOTATION -> "견적서 문서"
            SALES_ORDER -> "주문서 문서"
            SD_ETC -> "영업부 기타 알림"
            // IM (재고부)
            IM_ETC -> "재고 관련 알림 (부족, 입고, 출고 등)"
            // FCM (재무부)
            SALES_INVOICE -> "매출 청구서 (만기일 알림)"
            PURCHASE_INVOICE -> "매입 청구서 (만기일 알림)"
            FCM_ETC -> "재무부 기타 알림"
            // HRM (인사부)
            HRM_ETC -> "인사 관련 알림 (휴가, 급여, 교육 등)"
            // PP (생산부)
            ESTIMATE -> "견적서 문서"
            INSUFFICIENT_STOCK -> "가용 재고 부족 알림"
            PP_ETC -> "생산부 기타 알림"
        }

    /**
     * 관련 NotificationSourceEnum 반환
     * 이 링크 타입이 속한 소스를 반환합니다.
     */
    fun getRelatedSource(): NotificationSourceEnum =
        when (this) {
            UNKNOWN -> NotificationSourceEnum.UNKNOWN
            // PR (구매부)
            PURCHASE_REQUISITION,
            PURCHASE_ORDER,
            PR_ETC -> NotificationSourceEnum.PR
            // SD (영업부)
            QUOTATION,
            SALES_ORDER,
            SD_ETC -> NotificationSourceEnum.SD
            // IM (재고부)
            IM_ETC -> NotificationSourceEnum.IM
            // FCM (재무부)
            SALES_INVOICE,
            PURCHASE_INVOICE,
            FCM_ETC -> NotificationSourceEnum.FCM
            // HRM (인사부)
            HRM_ETC -> NotificationSourceEnum.HRM
            // PP (생산부)
            ESTIMATE,
            INSUFFICIENT_STOCK,
            PP_ETC -> NotificationSourceEnum.PP
        }

    /**
     * 화면 이동 가능 여부
     * ETC 타입은 화면 이동이 없음
     */
    fun hasNavigation(): Boolean =
        when (this) {
            UNKNOWN -> false
            PR_ETC,
            SD_ETC,
            IM_ETC,
            FCM_ETC,
            HRM_ETC,
            PP_ETC -> false
            else -> true
        }

    /**
     * 링크 코드 값 (서버에서 사용하는 실제 코드값)
     */
    val code: String get() = this.name

    companion object {
        /**
         * 문자열을 NotificationLinkEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): NotificationLinkEnum =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid NotificationLinkEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 NotificationLinkEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): NotificationLinkEnum? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 NotificationLinkEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: NotificationLinkEnum = UNKNOWN,
        ): NotificationLinkEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환 (API 필터링용)
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 특정 소스에 해당하는 링크 타입 목록 반환
         */
        fun getLinksBySource(source: NotificationSourceEnum): List<NotificationLinkEnum> =
            entries.filter { it.getRelatedSource() == source }

        /**
         * 화면 이동 가능한 링크 타입만 반환
         */
        fun getNavigableLinks(): List<NotificationLinkEnum> =
            entries.filter { it.hasNavigation() }
    }
}
