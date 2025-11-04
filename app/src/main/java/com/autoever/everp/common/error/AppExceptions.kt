package com.autoever.everp.common.error

/**
 * 앱 전반에서 공통으로 사용하는 예외 타입.
 */
open class AppException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/** 401 Unauthorized 표준화 예외 */
class UnauthorizedException(message: String = "Unauthorized", cause: Throwable? = null) : AppException(message, cause)

