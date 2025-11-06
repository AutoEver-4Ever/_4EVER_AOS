package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.autoever.everp.ui.common.components.QuickActionCard
import com.autoever.everp.ui.common.components.QuickActionIcons
import com.autoever.everp.ui.common.components.StatusBadge
import java.time.format.DateTimeFormatter

@Composable
fun CustomerHomeScreen(
    navController: NavController,
    viewModel: CustomerHomeViewModel = hiltViewModel(),
) {
    val recentQuotations by viewModel.recentQuotations.collectAsStateWithLifecycle()
    val recentOrders by viewModel.recentOrders.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            Text(
                text = "차량 외장재 관리",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        item {
            Text(
                text = "안녕하세요!",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "오늘도 효율적인 업무 관리를 시작해보세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            Text(
                text = "빠른 작업",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(200.dp),
            ) {
                item {
                    QuickActionCard(
                        icon = QuickActionIcons.QuotationRequest,
                        label = "견적 요청",
                        onClick = { /* TODO: 견적 요청 화면으로 이동 */ },
                    )
                }
                item {
                    QuickActionCard(
                        icon = QuickActionIcons.QuotationList,
                        label = "견적 목록",
                        onClick = { navController.navigate("customer_quotation") },
                    )
                }
                item {
                    QuickActionCard(
                        icon = QuickActionIcons.PurchaseOrderList,
                        label = "주문 관리",
                        onClick = { navController.navigate("customer_order") },
                    )
                }
                item {
                    QuickActionCard(
                        icon = QuickActionIcons.InvoiceList,
                        label = "매입전표",
                        onClick = { navController.navigate("customer_voucher") },
                    )
                }
            }
        }

        item {
            Text(
                text = "최근 활동",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        if (isLoading) {
            item {
                Text(text = "로딩 중...")
            }
        } else {
            // 견적서 활동
            recentQuotations.forEach { quotation ->
                item {
                    RecentActivityCard(
                        status = quotation.status.displayName(),
                        statusColor = quotation.status.toColor(),
                        title = "${quotation.number} - ${quotation.product.productId}",
                        date = quotation.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onClick = { /* TODO: 견적 상세 화면으로 이동 */ },
                    )
                }
            }

            // 주문서 활동
            recentOrders.forEach { order ->
                item {
                    RecentActivityCard(
                        status = order.statusCode.displayName(),
                        statusColor = order.statusCode.toColor(),
                        title = "${order.salesOrderNumber} - ${order.customerName}",
                        date = order.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onClick = { /* TODO: 주문 상세 화면으로 이동 */ },
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentActivityCard(
    status: String,
    statusColor: androidx.compose.ui.graphics.Color,
    title: String,
    date: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                StatusBadge(
                    text = status,
                    color = statusColor,
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "상세보기",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
