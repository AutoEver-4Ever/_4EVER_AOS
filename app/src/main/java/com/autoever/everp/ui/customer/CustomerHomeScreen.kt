package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.autoever.everp.domain.model.dashboard.DashboardTapEnum
import com.autoever.everp.ui.common.RecentActivityCard
import com.autoever.everp.ui.common.components.QuickActionCard
import com.autoever.everp.ui.common.components.QuickActionIcons
import com.autoever.everp.ui.common.navigateToWorkflowDetail
import timber.log.Timber
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    navController: NavController,
    viewModel: CustomerHomeViewModel = hiltViewModel(),
) {
    val recentActivities by viewModel.recentActivities.collectAsStateWithLifecycle()
    val categoryMap by viewModel.categoryMap.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val hasUnreadNotifications by viewModel.hasUnreadNotifications.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("차량 외장재 관리") },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(48.dp)
                            .padding(top = 16.dp, end = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(CustomerSubNavigationItem.NotificationItem.route)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "알림",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        // 읽지 않은 알림이 있으면 빨간색 점 표시
                        if (hasUnreadNotifications) {
                            Surface(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.TopEnd),
                                shape = CircleShape,
                                color = Color.Red,
                            ) {
                                // 빨간색 점
                            }
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
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
                            onClick = { navController.navigate(CustomerSubNavigationItem.QuotationCreateItem.route) },
                        )
                    }
                    item {
                        QuickActionCard(
                            icon = QuickActionIcons.QuotationList,
                            label = "견적 목록",
                            onClick = { navController.navigate(CustomerNavigationItem.Quotation.route) },
                        )
                    }
                    item {
                        QuickActionCard(
                            icon = QuickActionIcons.PurchaseOrderList,
                            label = "주문 관리",
                            onClick = { navController.navigate(CustomerNavigationItem.SalesOrder.route) },
                        )
                    }
                    item {
                        QuickActionCard(
                            icon = QuickActionIcons.InvoiceList,
                            label = "매입전표",
                            onClick = { navController.navigate(CustomerNavigationItem.Invoice.route) },
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
                                if (activity.tabCode.isCustomerRelated()) {
                                    navigateToWorkflowDetail(navController, category, activity.id)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
