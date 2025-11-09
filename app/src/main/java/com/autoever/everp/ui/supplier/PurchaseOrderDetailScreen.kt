package com.autoever.everp.ui.supplier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoever.everp.ui.common.components.StatusBadge
import com.autoever.everp.utils.state.UiResult
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderDetailScreen(
    navController: NavController,
    viewModel: PurchaseOrderDetailViewModel = hiltViewModel(),
) {
    val purchaseOrderDetail by viewModel.purchaseOrderDetail.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPurchaseOrderDetail()
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("발주서 상세 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
    ) { paddingValues ->
        when (uiState) {
            is UiResult.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiResult.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "오류가 발생했습니다: ${(uiState as UiResult.Error).exception.message}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            is UiResult.Success -> {
                purchaseOrderDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    ) {
                        // 발주서 정보 카드
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "발주서 정보",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )
                                DetailRow(
                                    label = "발주번호",
                                    value = detail.number,
                                )
                                DetailRow(
                                    label = "공급업체",
                                    value = detail.supplier.name,
                                )
                                DetailRow(
                                    label = "연락처",
                                    value = detail.supplier.managerPhone,
                                )
                                DetailRow(
                                    label = "이메일",
                                    value = detail.supplier.managerEmail,
                                )
                                DetailRow(
                                    label = "주문일자",
                                    value = detail.orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "납기일",
                                    value = detail.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "상태",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    StatusBadge(
                                        text = detail.status.displayName(),
                                        color = detail.status.toColor(),
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "총 금액",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "${formatCurrency(detail.totalAmount)}원",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 주문 품목 카드
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "주문 품목",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )

                                // 테이블 헤더
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = "품목명",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(2f),
                                    )
                                    Text(
                                        text = "규격",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1.5f),
                                    )
                                    Text(
                                        text = "수량",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        text = "단위",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        text = "단가",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        text = "금액",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // 테이블 구분선
                                androidx.compose.material3.Divider()

                                Spacer(modifier = Modifier.height(8.dp))

                                // 품목 리스트
                                detail.items.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(2f),
                                        )
                                        Text(
                                            text = item.uomName, // 규격은 uomName으로 대체
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1.5f),
                                        )
                                        Text(
                                            text = "${item.quantity}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                        )
                                        Text(
                                            text = item.uomName,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                        )
                                        Text(
                                            text = formatCurrency(item.unitPrice),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                        )
                                        Text(
                                            text = formatCurrency(item.totalPrice),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f),
                                        )
                                    }
                                    androidx.compose.material3.Divider()
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 총 금액
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "총 금액",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "${formatCurrency(detail.totalAmount)}원",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 배송 및 메모 정보 카드
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "배송 및 메모",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )
                                DetailRow(
                                    label = "배송 창고",
                                    value = "본사 창고", // TODO: 실제 데이터에서 가져오기
                                )
                                DetailRow(
                                    label = "요청 배송일",
                                    value = detail.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "특별 지시사항",
                                    value = "오전 배송 요청", // TODO: 실제 데이터에서 가져오기
                                )
                                if (detail.note.isNotEmpty()) {
                                    DetailRow(
                                        label = "메모",
                                        value = detail.note,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun formatCurrency(amount: Long): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(amount)
}
