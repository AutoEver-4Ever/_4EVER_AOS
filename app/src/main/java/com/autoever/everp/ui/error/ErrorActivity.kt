package com.autoever.everp.ui.error

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoever.everp.MainActivity
import com.autoever.everp.ui.theme.EverpTheme
import kotlin.system.exitProcess

/**
 * 앱 크래시 시 표시되는 에러 화면
 *
 * GlobalExceptionHandler에서 치명적인 오류 발생 시 호출됨
 */
class ErrorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errorCode = intent.getStringExtra(EXTRA_ERROR_CODE) ?: "ERR_UNKNOWN"
        val errorMessage = intent.getStringExtra(EXTRA_ERROR_MESSAGE)
            ?: "예상치 못한 오류가 발생했습니다."
        val isRetryable = intent.getBooleanExtra(EXTRA_IS_RETRYABLE, false)

        setContent {
            EverpTheme {
                ErrorScreen(
                    errorCode = errorCode,
                    errorMessage = errorMessage,
                    isRetryable = isRetryable,
                    onRestartClick = ::restartApp,
                    onExitClick = ::exitApp,
                )
            }
        }
    }

    /**
     * 앱 재시작
     */
    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()

        // 프로세스 완전 종료 후 재시작
        Process.killProcess(Process.myPid())
    }

    /**
     * 앱 종료
     */
    private fun exitApp() {
        finish()
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    /**
     * 뒤로가기 방지
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        // 뒤로가기 버튼 비활성화
        // 사용자가 명시적으로 선택하도록 함
    }

    companion object {
        const val EXTRA_ERROR_CODE = "error_code"
        const val EXTRA_ERROR_MESSAGE = "error_message"
        const val EXTRA_IS_RETRYABLE = "is_retryable"
    }
}

@Composable
private fun ErrorScreen(
    errorCode: String,
    errorMessage: String,
    isRetryable: Boolean,
    onRestartClick: () -> Unit,
    onExitClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // 아이콘
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = "Error",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 제목
            Text(
                text = "앱에 문제가 발생했습니다",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 에러 메시지
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 에러 코드 (디버깅용)
            Text(
                text = "오류 코드: $errorCode",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 재시도 가능 여부에 따른 안내
            if (isRetryable) {
                Text(
                    text = "앱을 다시 시작하면 문제가 해결될 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                Text(
                    text = "앱을 다시 시작해주세요.\n문제가 계속되면 고객센터로 문의해주세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 버튼들
            Button(
                onClick = onRestartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text("앱 다시 시작")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onExitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text("종료")
            }
        }
    }
}

