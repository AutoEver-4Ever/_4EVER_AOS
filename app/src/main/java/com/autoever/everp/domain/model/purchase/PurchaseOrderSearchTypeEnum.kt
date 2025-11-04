package com.autoever.everp.domain.model.purchase

enum class PurchaseOrderSearchTypeEnum {
    UNKNOWN, // 알 수 없음, 기본값
    SupplierCompanyName, // 공급사 회사 이름
    PurchaseOrderNumber, // 구매 주문 번호
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            SupplierCompanyName -> "공급사명"
            PurchaseOrderNumber -> "발주번호"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열
     */
    fun toApiString(): String = this.name

    /**
     * 검색 유형 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 검색 유형"
            SupplierCompanyName -> "공급사 회사 이름으로 검색"
            PurchaseOrderNumber -> "발주 번호로 검색"
        }

    /**
     * 플레이스홀더 텍스트 (검색창에 표시할 힌트)
     */
    fun getPlaceholder(): String =
        when (this) {
            UNKNOWN -> "검색어를 입력하세요"
            SupplierCompanyName -> "공급사명을 입력하세요"
            PurchaseOrderNumber -> "발주번호를 입력하세요"
        }

    /**
     * 검색 유형 코드 값
     */
    val code: String get() = this.name

    /**
     * 유효한 검색 유형인지 확인 (UNKNOWN 제외)
     */
    fun isValid(): Boolean = this != UNKNOWN

    /**
     * 공급사명 검색 여부
     */
    fun isSupplierSearch(): Boolean = this == SupplierCompanyName

    /**
     * 발주번호 검색 여부
     */
    fun isPurchaseOrderNumberSearch(): Boolean = this == PurchaseOrderNumber

    companion object {
        /**
         * 문자열을 PurchaseOrderSearchTypeEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): PurchaseOrderSearchTypeEnum =
            try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid PurchaseOrderSearchTypeEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 PurchaseOrderSearchTypeEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): PurchaseOrderSearchTypeEnum? =
            try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 PurchaseOrderSearchTypeEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: PurchaseOrderSearchTypeEnum = UNKNOWN,
        ): PurchaseOrderSearchTypeEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 유효한 검색 유형 목록 (UNKNOWN 제외)
         */
        fun getValidSearchTypes(): List<PurchaseOrderSearchTypeEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * UI 표시용 검색 유형 목록 (드롭다운 등에서 사용)
         * Pair<검색 유형, 한글명>
         */
        fun getSearchTypeOptions(): List<Pair<PurchaseOrderSearchTypeEnum, String>> =
            getValidSearchTypes().map { it to it.toKorean() }
    }
}
