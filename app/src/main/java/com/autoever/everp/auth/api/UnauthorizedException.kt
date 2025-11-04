package com.autoever.everp.auth.api

/**
 * 401 Unauthorized 시그널용 예외.
 */
class UnauthorizedException(message: String = "Unauthorized") : RuntimeException(message)

