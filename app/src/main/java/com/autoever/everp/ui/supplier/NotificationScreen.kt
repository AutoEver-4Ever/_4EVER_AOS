package com.autoever.everp.ui.supplier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.autoever.everp.domain.model.notification.Notification
import com.autoever.everp.domain.model.notification.NotificationLinkEnum
import com.autoever.everp.ui.common.components.StatusBadge
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = error ?: "오류가 발생했습니다.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            notifications.content.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "알림이 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(notifications.content) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                // 알림 클릭 시 읽음 처리 및 상세 화면 이동
                                viewModel.markAsRead(notification.id)
                                navigateToDetail(navController, notification)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                )

                StatusBadge(
                    text = notification.source.toKorean(),
                    color = notification.source.toColor(),
                )
            }

            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatRelativeTime(notification.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun navigateToDetail(
    navController: NavController,
    notification: Notification,
) {
    if (!notification.isNavigable || notification.linkId == null) {
        return
    }

    when (notification.linkType) {
        NotificationLinkEnum.PURCHASE_ORDER -> {
            navController.navigate(
                SupplierSubNavigationItem.PurchaseOrderDetailItem.createRoute(notification.linkId),
            )
        }

        NotificationLinkEnum.PURCHASE_INVOICE -> {
            navController.navigate(
                SupplierSubNavigationItem.InvoiceDetailItem.createRoute(
                    invoiceId = notification.linkId,
                    isAp = true,
                ),
            )
        }

        else -> {
            // Supplier 화면에서는 발주와 매입 전표만 이동
        }
    }
}

private fun formatRelativeTime(createdAt: LocalDateTime): String {
    val now = LocalDateTime.now()
    val duration = Duration.between(createdAt, now)

    return when {
        duration.toSeconds() < 60 -> {
            "${duration.toSeconds()}초 전"
        }

        duration.toMinutes() < 60 -> {
            "${duration.toMinutes()}분 전"
        }

        duration.toHours() < 24 -> {
            "${duration.toHours()}시간 전"
        }

        duration.toDays() < 30 -> {
            "${duration.toDays()}일 전"
        }

        ChronoUnit.MONTHS.between(createdAt, now) < 12 -> {
            "${ChronoUnit.MONTHS.between(createdAt, now)}개월 전"
        }

        else -> {
            "${ChronoUnit.YEARS.between(createdAt, now)}년 전"
        }
    }
}
