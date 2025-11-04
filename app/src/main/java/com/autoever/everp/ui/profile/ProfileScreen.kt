package com.autoever.everp.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
) {
    val ui by vm.ui.collectAsState()
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (ui.isLoading) {
                CircularProgressIndicator()
            } else if (ui.errorMessage != null) {
                Text(
                    text = ui.errorMessage ?: "오류",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.refresh() }) { Text("다시 시도") }
            } else {
                Text(
                    text = "프로필",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(16.dp))
                Text("이름: ${ui.user?.userName ?: "-"}")
                Text("이메일: ${ui.user?.loginEmail ?: "-"}")
                Text("역할: ${ui.user?.userRole ?: "-"}")
                Text("유형: ${ui.user?.userType ?: "-"}")
                Spacer(Modifier.height(24.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { vm.logout() },
                ) { Text("로그아웃") }
            }
        }
    }
}

