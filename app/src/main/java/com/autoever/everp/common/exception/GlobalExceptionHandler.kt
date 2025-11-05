package com.autoever.everp.common.exception

import android.content.Context
import android.content.Intent
import android.os.Process
import com.autoever.everp.domain.exception.ExceptionMapper
import com.autoever.everp.domain.exception.EverpException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

/**
 * 전역 예외 처리기
 * 
 * ### 역할
 * 1. **앱 크래시 방지**: 처리되지 않은 예외를 잡아 앱 강제 종료 방지
 * 2. **로그 기록**: Crashlytics와 Timber에 예외 정보 기록
 * 3. **사용자 알림**: 오류 화면으로 이동하여 사용자에게 안내
 * 
 * ### 주의사항
 * - 이 핸들러는 **마지막 방어선**입니다
 * - Repository/ViewModel에서 적절히 처리한 예외는 여기까지 오지 않습니다
 * - 여기서 잡히는 예외는 주로 코드 버그나 예상치 못한 오류입니다
 */
@Singleton
class GlobalExceptionHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crashlytics: FirebaseCrashlytics,
) : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 기본 핸들러 저장 (Application에서 설정)
     */
    fun initialize() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        Timber.tag(TAG).i("GlobalExceptionHandler initialized")
    }

    /**
     * 처리되지 않은 예외 처리
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            Timber.tag(TAG).e(throwable, "========== UNCAUGHT EXCEPTION ==========")
            Timber.tag(TAG).e("Thread: ${thread.name}")
            Timber.tag(TAG).e("Exception: ${throwable.javaClass.simpleName}")
            Timber.tag(TAG).e("Message: ${throwable.message}")
            
            // EverpException으로 변환하여 분류
            val everpException = if (throwable is EverpException) {
                throwable
            } else {
                ExceptionMapper.mapToEverpException(throwable)
            }
            
            Timber.tag(TAG).e("Mapped to: ${everpException.javaClass.simpleName}")
            Timber.tag(TAG).e("Error Code: ${everpException.getErrorCode()}")
            Timber.tag(TAG).e("User Message: ${everpException.getUserMessage()}")
            
            // Crashlytics에 기록
            recordToCrashlytics(thread, throwable, everpException)
            
            // 에러 화면 표시 (사용자에게 알림)
            showErrorActivity(everpException)
            
        } catch (e: Exception) {
            // 예외 처리 중 오류 발생 시 로그만 기록
            Timber.tag(TAG).e(e, "Error while handling uncaught exception")
        } finally {
            // 잠시 대기하여 ErrorActivity가 시작될 시간 확보
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                // 무시
            }
            
            // 기본 핸들러 호출 (앱 종료)
            // ErrorActivity가 떠있는 상태에서 기본 핸들러가 호출되지만
            // ErrorActivity는 새로운 태스크로 실행되었기 때문에 유지됨
            defaultHandler?.uncaughtException(thread, throwable)
                ?: run {
                    // 기본 핸들러가 없으면 직접 종료
                    Process.killProcess(Process.myPid())
                    exitProcess(10)
                }
        }
    }

    /**
     * Crashlytics에 예외 기록
     */
    private fun recordToCrashlytics(
        thread: Thread,
        originalException: Throwable,
        everpException: EverpException,
    ) {
        try {
            // 커스텀 키 설정
            crashlytics.setCustomKey("thread_name", thread.name)
            crashlytics.setCustomKey("error_code", everpException.getErrorCode())
            crashlytics.setCustomKey("user_message", everpException.getUserMessage())
            crashlytics.setCustomKey("is_retryable", everpException.isRetryable())
            crashlytics.setCustomKey("exception_type", everpException.javaClass.simpleName)
            
            // 원본 예외 기록 (stackTrace 포함)
            crashlytics.recordException(originalException)
            
            Timber.tag(TAG).i("Exception recorded to Crashlytics")
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to record to Crashlytics")
        }
    }

    /**
     * 에러 화면으로 이동
     * 
     * ### 동작 방식
     * 1. ErrorActivity를 새로운 태스크로 실행
     * 2. 기존 액티비티 스택 모두 제거
     * 3. 사용자에게 오류 정보 표시
     * 4. 재시작 또는 종료 선택 가능
     * 
     * ### 주의사항
     * - Activity가 없는 상태에서도 실행 가능 (FLAG_ACTIVITY_NEW_TASK)
     * - 뒤로가기로 돌아갈 수 없음 (FLAG_ACTIVITY_CLEAR_TASK)
     * - 앱 재시작 또는 종료만 가능
     */
    private fun showErrorActivity(everpException: EverpException) {
        try {
            val errorActivityClass = Class.forName("com.autoever.everp.ui.error.ErrorActivity")
            val intent = Intent(context, errorActivityClass).apply {
                // 새로운 태스크로 실행하고 기존 액티비티 스택 제거
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                
                // 에러 정보 전달
                putExtra("error_code", everpException.getErrorCode())
                putExtra("error_message", everpException.getUserMessage())
                putExtra("is_retryable", everpException.isRetryable())
            }
            
            context.startActivity(intent)
            Timber.tag(TAG).i("Error activity started")
            
        } catch (e: Exception) {
            // ErrorActivity가 없거나 실행 실패 시 로그만 기록하고 계속 진행
            Timber.tag(TAG).e(e, "Failed to show error activity")
        }
    }

    companion object {
        private const val TAG = "GlobalExceptionHandler"
    }
}
