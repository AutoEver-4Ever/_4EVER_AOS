package com.autoever.everp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.autoever.everp.domain.model.dashboard.DashboardTapEnum
import com.autoever.everp.ui.common.components.StatusBadge
import com.autoever.everp.ui.customer.CustomerSubNavigationItem
import com.autoever.everp.ui.supplier.SupplierSubNavigationItem

/**
 * 최근 활동 카드 컴포저블
 *
 * TODO CUSTOMER, SUPPLIER 공통으로 이용하니 그에 따른 수정 필요
 */

@Composable
fun RecentActivityCard(
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

fun navigateToWorkflowDetail(
    navController: NavController,
    category: DashboardTapEnum,
    workflowId: String,
) {
    when (category) {
        DashboardTapEnum.QT -> { // 견적
            navController.navigate(
                CustomerSubNavigationItem.QuotationDetailItem.createRoute(quotationId = workflowId)
            )
        }

        DashboardTapEnum.SO -> { // 주문
            navController.navigate(
                CustomerSubNavigationItem.SalesOrderDetailItem.createRoute(workflowId)
            )
        }

        DashboardTapEnum.AP -> { // 매입
            navController.navigate(
                CustomerSubNavigationItem.InvoiceDetailItem.createRoute(
                    invoiceId = workflowId,
                    isAp = true,
                ),
            )
        }

        DashboardTapEnum.AR -> { // 매출
            navController.navigate(
                SupplierSubNavigationItem.InvoiceDetailItem.createRoute(
                    invoiceId = workflowId,
                    isAp = false,
                )
            )
        }

        DashboardTapEnum.PO -> { // 발주
            navController.navigate(
                SupplierSubNavigationItem.PurchaseOrderDetailItem.createRoute(
                    workflowId
                )
            )
        }
        // Customer 화면에서는 발주, 구매 등 상세로 이동하지 않음
        else -> {
            // 알 수 없는 카테고리 또는 이동 불가능한 카테고리
        }
    }
}

/*
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

 */
