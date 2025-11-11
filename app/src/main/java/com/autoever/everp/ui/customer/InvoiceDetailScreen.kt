package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.autoever.everp.domain.model.invoice.InvoiceStatusEnum
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
import java.time.format.DateTimeFormatter

@Composable
fun InvoiceDetailScreen(
    navController: NavController,
    invoiceId: String,
    isAp: Boolean = false, // false면 AR(매출), true면 AP(매입)
    viewModel: InvoiceDetailViewModel = hiltViewModel(),
) {
    val invoiceDetail by viewModel.invoiceDetail.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val requestResult by viewModel.requestResult.collectAsState()

    LaunchedEffect(invoiceId, isAp) {
        viewModel.loadInvoiceDetail(invoiceId, isAp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                )
            }
            Text(
                text = "전표 상세",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        when (uiState) {
            is UiResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiResult.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "오류가 발생했습니다: ${(uiState as UiResult.Error).exception.message}",
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Button(onClick = { viewModel.retry(invoiceId, isAp) }) {
                        Text("다시 시도")
                    }
                }
            }

            is UiResult.Success -> {
                invoiceDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        // 전표 정보 카드
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = detail.number,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    StatusBadge(
                                        text = detail.status.displayName(),
                                        color = detail.status.toColor(),
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                DetailRow(
                                    label = "전표유형",
                                    value = detail.type.displayName(),
                                )
                                DetailRow(
                                    label = "거래처",
                                    value = detail.connectionName,
                                )
                                DetailRow(
                                    label = "전표 발생일",
                                    value = detail.issueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "만기일",
                                    value = detail.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "참조번호",
                                    value = detail.referenceNumber,
                                )
                                if (detail.note.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DetailRow(
                                        label = "메모",
                                        value = detail.note,
                                    )
                                }
                            }
                        }

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
                                        text = "품목",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(2f),
                                    )
                                    Text(
                                        text = "수량",
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

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                // 테이블 아이템
                                detail.items.forEach { item ->
                                    InvoiceItemRow(item = item)
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }

                                // 총 금액
                                Spacer(modifier = Modifier.height(8.dp))
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

                        // 납부 확인 요청 버튼 (UNPAID 상태일 때만 표시)
                        if (detail.status == InvoiceStatusEnum.UNPAID) {
//                            if (isAp && detail.status == InvoiceStatusEnum.UNPAID) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.updateCustomerInvoiceStatus(invoiceId) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text("납부 확인 요청")
                            }
                        }
                    }
                }
            }

            else -> {}
        }

        // 결과 모달 다이얼로그
        requestResult?.let { result ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.clearRequestResult()
                    if (result.isSuccess) {
                        navController.popBackStack()
                    }
                },
                title = {
                    Text(
                        if (result.isSuccess) "요청 완료" else "요청 실패",
                    )
                },
                text = {
                    Text(
                        if (result.isSuccess) {
                            "납부 확인 요청이 완료되었습니다."
                        } else {
                            result.exceptionOrNull()?.message ?: "요청 처리 중 오류가 발생했습니다."
                        },
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearRequestResult()
                            if (result.isSuccess) {
                                navController.popBackStack()
                            }
                        },
                    ) {
                        Text("확인")
                    }
                },
            )
        }
    }
}

@Composable
private fun InvoiceItemRow(item: com.autoever.everp.domain.model.invoice.InvoiceDetail.InvoiceDetailItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = item.unitOfMaterialName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = "${item.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatCurrency(item.unitPrice),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${formatCurrency(item.totalPrice)}원",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    valueFontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = valueFontWeight,
        )
    }
}
