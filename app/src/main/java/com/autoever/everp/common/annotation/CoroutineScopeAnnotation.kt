package com.autoever.everp.common.annotation

import javax.inject.Qualifier

// 일회용 -> 절대 쓰지 말기
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

