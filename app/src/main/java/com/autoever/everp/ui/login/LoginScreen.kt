package com.autoever.everp.ui.login

import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import com.autoever.everp.ui.theme.EverpTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import com.autoever.everp.R

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
) {
    val brand = Color(red = 55 / 255f, green = 83 / 255f, blue = 150 / 255f)
    var isLoading by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .navigationBarsPadding()
        ) {
            // 로고: 중앙 상단
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.everp_logo),
                contentDescription = "EVERP 로고",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 360.dp)
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )

            // 카피: 중앙
            Text(
                text = "하나의 계정으로 ERP 서비스를 이용하세요",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 60.dp)
            )

            // 버튼: 중앙 하단
            Button(
                onClick = {
                    if (!isLoading) {
                        isLoading = true
                        onLoginClick()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brand),
                enabled = !isLoading,
            ) {
                Text(
                    text = if (isLoading) "브라우저를 여는 중..." else "로그인하기",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Login – Light")
@Composable
private fun PreviewLoginScreen() {
    EverpTheme {
        LoginScreen(onLoginClick = {})
    }
}
