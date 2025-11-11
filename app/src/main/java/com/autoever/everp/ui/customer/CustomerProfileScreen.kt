package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CustomerProfileScreen(
    loginNavController: NavController,
    navController: NavController,
    viewModel: CustomerProfileViewModel = hiltViewModel(),
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "프로필",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            androidx.compose.material3.TextButton(
                onClick = {
                    navController.navigate(CustomerSubNavigationItem.ProfileEditItem.route)
                },
            ) {
                Text("편집")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 사용자 프로필 아이콘
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "프로필",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            tint = MaterialTheme.colorScheme.primary,
        )

        // 사용자 이름과 직책
        Text(
            text = userInfo?.userName ?: "로딩 중...",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
        )

        Text(
            text = "${userInfo?.userType?.name ?: ""}·${userInfo?.userRole?.name ?: ""}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 고객사 정보 섹션
        Text(
            text = "고객사 정보",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                ProfileField(
                    label = "회사명 *",
                    value = profile?.companyName ?: "",
                )
                ProfileField(
                    label = "사업자등록번호",
                    value = profile?.businessNumber ?: "",
                )
                ProfileField(
                    label = "회사 주소",
                    value = profile?.fullAddress ?: "",
                )
                ProfileField(
                    label = "회사 전화번호",
                    value = profile?.officePhone ?: "",
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 개인 정보 섹션
        Text(
            text = "개인 정보",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                ProfileField(
                    label = "이름 *",
                    value = profile?.userName ?: userInfo?.userName ?: "",
                )
                ProfileField(
                    label = "이메일 *",
                    value = profile?.userEmail ?: userInfo?.email ?: "",
                )
                ProfileField(
                    label = "휴대폰 번호",
                    value = profile?.userPhoneNumber ?: "",
                )
            }
        }

        Button(
            onClick = {
                viewModel.logout {
                    loginNavController.navigate("login") {
                        popUpTo(0)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        ) { }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { /* 편집 모드에서만 */ },
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    )
}
