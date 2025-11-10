package com.autoever.everp.ui.supplier

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierProfileEditScreen(
    navController: NavController,
    viewModel: SupplierProfileEditViewModel = hiltViewModel(),
) {
    val profile by viewModel.profile.collectAsState()
    val userInfo by viewModel.userInfo.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    var companyName by remember { mutableStateOf("") }
    var businessNumber by remember { mutableStateOf("") }
    var baseAddress by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }
    var officePhone by remember { mutableStateOf("") }
    var userPhoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        profile?.let { p ->
            companyName = p.companyName
            businessNumber = p.businessNumber
            baseAddress = p.baseAddress
            detailAddress = p.detailAddress
            officePhone = p.officePhone
            userPhoneNumber = p.userPhoneNumber
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("프로필 편집") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // 사업자 정보 섹션
            Text(
                text = "사업자 정보",
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
                    OutlinedTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("회사명 *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = businessNumber,
                        onValueChange = { businessNumber = it },
                        label = { Text("사업자등록번호") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = baseAddress,
                        onValueChange = { baseAddress = it },
                        label = { Text("기본 주소") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = detailAddress,
                        onValueChange = { detailAddress = it },
                        label = { Text("상세 주소") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = officePhone,
                        onValueChange = { officePhone = it },
                        label = { Text("회사 전화번호") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                    OutlinedTextField(
                        value = userInfo?.userName ?: "",
                        onValueChange = { /* 이름은 수정 불가 */ },
                        label = { Text("이름 *") },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = profile?.userEmail ?: userInfo?.email ?: "",
                        onValueChange = { /* 이메일은 수정 불가 */ },
                        label = { Text("이메일 *") },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                    OutlinedTextField(
                        value = userPhoneNumber,
                        onValueChange = { userPhoneNumber = it },
                        label = { Text("휴대폰 번호") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 저장 버튼
            Button(
                onClick = {
                    viewModel.saveProfile(
                        companyName = companyName,
                        businessNumber = businessNumber,
                        baseAddress = baseAddress,
                        detailAddress = detailAddress,
                        officePhone = officePhone,
                        userPhoneNumber = userPhoneNumber,
                        onSuccess = {
                            navController.popBackStack()
                        },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
            ) {
                if (isSaving) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
                Text(if (isSaving) "저장 중..." else "저장")
            }
        }
    }
}
