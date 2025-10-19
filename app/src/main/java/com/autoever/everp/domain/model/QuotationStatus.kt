package com.autoever.everp.domain.model

enum class QuotationStatus {
    PENDING,
    REVIEW,
    APPROVED,
    REJECTED,
    ;

    fun displayName(): String =
        when (this) {
            PENDING -> "대기"
            REVIEW -> "검토"
            APPROVED -> "승인"
            REJECTED -> "반려"
        }

    /**
     * Enum을 문자열로 변환 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 상태를 한글로 변환
     */
    fun toKorean(): String = displayName()

    companion object {
        /**
         * 문자열을 Enum으로 변환
         * 매핑되지 않는 값이면 IllegalArgumentException 발생
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): QuotationStatus =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid QuotationStatus: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 Enum으로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): QuotationStatus? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 Enum으로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: QuotationStatus = PENDING,
        ): QuotationStatus = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }
    }
}
