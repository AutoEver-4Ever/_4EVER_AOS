package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoever.everp.ui.common.components.ListCard
import com.autoever.everp.ui.common.components.SearchBar
import com.autoever.everp.ui.common.components.StatusBadge
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CustomerOrderScreen(
    navController: NavController,
    viewModel: CustomerOrderViewModel = hiltViewModel(),
) {
    val orderList by viewModel.orderList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // 헤더
        Text(
            text = "주문 관리",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        // 검색 바
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            placeholder = "주문번호로 검색",
            onSearch = { viewModel.search() },
        )

        // 리스트
        if (isLoading) {
            Text(
                text = "로딩 중...",
                modifier = Modifier.padding(16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(orderList.content) { order ->
                    ListCard(
                        id = order.salesOrderNumber,
                        title = "${order.customerName} - ${order.managerName}",
                        statusBadge = {
                            StatusBadge(
                                text = order.statusCode.displayName(),
                                color = order.statusCode.toColor(),
                            )
                        },
                        details = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = "납기일: ${order.dueDate}",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = "주문금액: ${formatCurrency(order.totalAmount)}원",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                        trailingContent = {
                            Button(
                                onClick = { /* TODO: 주문 상세 화면으로 이동 */ },
                                modifier = Modifier.padding(top = 8.dp),
                            ) {
                                Text("상세보기")
                            }
                        },
                        onClick = { /* TODO: 주문 상세 화면으로 이동 */ },
                    )
                }
            }
        }
    }
}

private fun formatCurrency(amount: Long): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(amount)
}
