package com.autoever.everp.domain.model.user

enum class UserRole {
    CUSTOMER, // 고객사
    VENDOR, // 공급사
    NONE, // 미설정
    ;

    /**
     * 역할을 한글로 변환
     */
    fun toKorean(): String =
        when (this) {
            CUSTOMER -> "고객사"
            VENDOR -> "공급사"
            NONE -> "미설정"
        }

    /**
     * 역할을 영어로 변환 (대문자)
     */
    fun toApiString(): String = this.name

    /**
     * 역할 표시 이름
     */
    fun displayName(): String = toKorean()

    companion object {
        /**
         * 문자열을 UserRole로 변환
         * @throws IllegalArgumentException
         */
        fun fromString(value: String): UserRole =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Invalid UserRole: '$value'. " +
                        "Valid values are: ${entries.joinToString { it.name }}",
                )
            }

        /**
         * 문자열을 UserRole로 안전하게 변환 (null 반환)
         */
        fun fromStringOrNull(value: String): UserRole? =
            try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }

        /**
         * 문자열을 UserRole로 변환 (기본값 제공)
         */
        fun fromStringOrDefault(
            value: String,
            default: UserRole = NONE,
        ): UserRole = fromStringOrNull(value) ?: default

        /**
         * 모든 enum 값을 문자열 리스트로 반환
         */
        fun getAllValues(): List<String> = entries.map { it.name }
    }
}
