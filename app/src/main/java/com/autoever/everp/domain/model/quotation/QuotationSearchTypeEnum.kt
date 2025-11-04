package com.autoever.everp.domain.model.quotation

enum class QuotationSearchTypeEnum {
    UNKNOWN, // 알 수 없음, 기본값
    quotationNumber, // 견적번호
    customerName, // 고객사명
    managerName, // 담당자명
    ;

    /**
     * UI 표시용 한글명
     */
    fun toKorean(): String =
        when (this) {
            UNKNOWN -> "알 수 없음"
            quotationNumber -> "견적번호"
            customerName -> "고객사명"
            managerName -> "담당자명"
        }

    /**
     * 표시 이름 (toKorean과 동일하지만 명확성을 위해)
     */
    fun displayName(): String = toKorean()

    /**
     * API 통신용 문자열
     */
    fun toApiString(): String? = if (this != UNKNOWN) this.name else null

    /**
     * 검색 유형 설명
     */
    fun description(): String =
        when (this) {
            UNKNOWN -> "알 수 없는 검색 유형"
            quotationNumber -> "견적 번호로 검색"
            customerName -> "고객사 이름으로 검색"
            managerName -> "담당자 이름으로 검색"
        }

    /**
     * 플레이스홀더 텍스트 (검색창에 표시할 힌트)
     */
    fun getPlaceholder(): String =
        when (this) {
            UNKNOWN -> "검색어를 입력하세요"
            quotationNumber -> "견적번호를 입력하세요"
            customerName -> "고객사명을 입력하세요"
            managerName -> "담당자명을 입력하세요"
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
     * 견적번호 검색 여부
     */
    fun isQuotationNumberSearch(): Boolean = this == quotationNumber

    /**
     * 고객사명 검색 여부
     */
    fun isCustomerNameSearch(): Boolean = this == customerName

    /**
     * 담당자명 검색 여부
     */
    fun isManagerNameSearch(): Boolean = this == managerName

    companion object {
        /**
         * 문자열을 QuotationSearchTypeEnum으로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): QuotationSearchTypeEnum =
            try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid QuotationSearchTypeEnum: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 QuotationSearchTypeEnum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): QuotationSearchTypeEnum? =
            try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 QuotationSearchTypeEnum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: QuotationSearchTypeEnum = UNKNOWN,
        ): QuotationSearchTypeEnum = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }

        /**
         * 유효한 검색 유형 목록 (UNKNOWN 제외)
         */
        fun getValidSearchTypes(): List<QuotationSearchTypeEnum> =
            entries.filter { it != UNKNOWN }

        /**
         * UI 표시용 검색 유형 목록 (드롭다운 등에서 사용)
         * Pair<검색 유형, 한글명>
         */
        fun getSearchTypeOptions(): List<Pair<QuotationSearchTypeEnum, String>> =
            getValidSearchTypes().map { it to it.toKorean() }
    }
}
