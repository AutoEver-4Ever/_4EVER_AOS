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
import com.autoever.everp.domain.model.dashboard.DashboardTapEnum
import com.autoever.everp.ui.common.RecentActivityCard
import com.autoever.everp.ui.common.components.QuickActionCard
import com.autoever.everp.ui.common.components.QuickActionIcons
import com.autoever.everp.ui.common.components.StatusBadge
import com.autoever.everp.ui.common.navigateToWorkflowDetail
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
                    val category = categoryMap[activity.id] ?: DashboardTapEnum.UNKNOWN
                    RecentActivityCard(
                        category = category.toKorean(),
                        status = activity.status,
                        title = activity.description,
                        date = activity.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        onClick = {
                            navigateToWorkflowDetail(navController, category, activity.id)
                        },
                    )
                }
            }
        }
    }
}
