package com.autoever.everp.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoever.everp.ui.common.components.StatusBadge
import com.autoever.everp.utils.state.UiResult
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SalesOrderDetailScreen(
    navController: NavController,
    salesOrderId: String,
    viewModel: SalesOrderDetailViewModel = hiltViewModel(),
) {
    val salesOrderDetail by viewModel.salesOrderDetail.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(salesOrderId) {
        viewModel.loadSalesOrderDetail(salesOrderId)
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
                text = "주문 상세",
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
                    Button(onClick = { viewModel.retry(salesOrderId) }) {
                        Text("다시 시도")
                    }
                }
            }

            is UiResult.Success -> {
                salesOrderDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        // 주문 정보 카드
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
                                        text = detail.salesOrderNumber,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    StatusBadge(
                                        text = detail.statusCode.displayName(),
                                        color = detail.statusCode.toColor(),
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                DetailRow(
                                    label = "주문일자",
                                    value = detail.orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "납기일",
                                    value = detail.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                )
                                DetailRow(
                                    label = "주문금액",
                                    value = "${formatCurrency(detail.totalAmount)}원",
                                    valueColor = MaterialTheme.colorScheme.primary,
                                    valueFontWeight = FontWeight.Bold,
                                )
                                // 운송장번호는 현재 데이터 모델에 없음 (추후 추가 가능)
                            }
                        }

                        // 주문 진행 상태 카드
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
                                    text = "주문 진행 상태",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )

                                val statuses = com.autoever.everp.domain.model.sale.SalesOrderStatusEnum.entries
                                    .filter { it != com.autoever.everp.domain.model.sale.SalesOrderStatusEnum.UNKNOWN }
                                val currentStatusIndex = statuses.indexOf(detail.statusCode)

                                statuses.forEachIndexed { index, status ->
                                    OrderStatusItem(
                                        status = status,
                                        isCompleted = index <= currentStatusIndex,
                                    )
                                    if (index < statuses.size - 1) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }

                        // 고객 정보 카드
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
                                    text = "고객 정보",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )
                                DetailRow(
                                    label = "고객명",
                                    value = detail.customerName,
                                )
                                DetailRow(
                                    label = "담당자",
                                    value = detail.managerName,
                                )
                                DetailRow(
                                    label = "이메일",
                                    value = detail.managerEmail,
                                )
                            }
                        }

                        // 배송 정보 카드
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
                                    text = "배송 정보",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                )
                                DetailRow(
                                    label = "배송지",
                                    value = detail.fullAddress,
                                )
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

                                detail.items.forEach { item ->
                                    OrderItemRow(item = item)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }

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
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun OrderStatusItem(
    status: com.autoever.everp.domain.model.sale.SalesOrderStatusEnum,
    isCompleted: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) MaterialTheme.colorScheme.primary
                    else Color(0xFF9E9E9E),
                ),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = status.displayName(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isCompleted) MaterialTheme.colorScheme.onSurface
            else Color(0xFF9E9E9E),
        )
    }
}

@Composable
private fun OrderItemRow(item: com.autoever.everp.domain.model.sale.SalesOrderItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = item.itemName,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "수량: ${item.quantity}개",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "단가: ${formatCurrency(item.unitPrice)}원",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "${formatCurrency(item.totalPrice)}원",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
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
