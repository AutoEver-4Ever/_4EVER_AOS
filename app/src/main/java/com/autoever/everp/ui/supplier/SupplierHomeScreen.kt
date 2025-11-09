package com.autoever.everp.ui.supplier

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
fun SupplierHomeScreen(
    navController: NavController,
    viewModel: SupplierHomeViewModel = hiltViewModel(),
) {
    val recentActivities by viewModel.recentActivities.collectAsStateWithLifecycle()
    val categoryMap by viewModel.categoryMap.collectAsStateWithLifecycle()
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
                        icon = QuickActionIcons.PurchaseOrderList,
                        label = "발주",
                        onClick = { navController.navigate("supplier_purchase_order") },
                    )
                }
                item {
                    QuickActionCard(
                        icon = QuickActionIcons.InvoiceList,
                        label = "전표",
                        onClick = { navController.navigate("supplier_invoice") },
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
            recentActivities.forEach { activity ->
                item {
                    val category = categoryMap[activity.id] ?: ""
                    RecentActivityCard(
                        category = getCategoryDisplayName(category),
                        status = activity.status,
                        title = activity.description,
                        date = activity.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onClick = {
                            navigateToDetail(navController, category, activity.id)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentActivityCard(
    category: String,
    status: String,
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
                // Category badge (파란색)
                StatusBadge(
                    text = category,
                    color = androidx.compose.ui.graphics.Color(0xFF2196F3),
                )
                // Status badge (회색)
                StatusBadge(
                    text = status,
                    color = androidx.compose.ui.graphics.Color(0xFF9E9E9E),
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

private fun getCategoryDisplayName(tabCode: String): String {
    return when (tabCode.uppercase()) {
        "QUOTATION", "견적" -> "견적"
        "ORDER", "주문", "SALES_ORDER" -> "주문"
        "INVOICE", "전표", "AP_INVOICE", "AR_INVOICE" -> "전표"
        "PURCHASE_ORDER", "발주" -> "발주"
        else -> tabCode
    }
}

private fun navigateToDetail(
    navController: NavController,
    category: String,
    workflowId: String,
) {
    when (category.uppercase()) {
        "PURCHASE_ORDER", "발주" -> {
            navController.navigate(
                SupplierSubNavigationItem.PurchaseOrderDetailItem.createRoute(workflowId)
            )
        }
        "INVOICE", "전표", "AP_INVOICE", "AR_INVOICE" -> {
            navController.navigate(
                SupplierSubNavigationItem.InvoiceDetailItem.createRoute(
                    invoiceId = workflowId,
                    isAp = category.contains("AP", ignoreCase = true),
                ),
            )
        }
    }
}
