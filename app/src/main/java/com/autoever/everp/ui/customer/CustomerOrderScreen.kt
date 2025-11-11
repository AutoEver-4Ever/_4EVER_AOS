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
import com.autoever.everp.domain.model.sale.SalesOrderSearchTypeEnum
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
    val searchParams by viewModel.searchParams.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

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
            query = searchParams.searchKeyword,
            onQueryChange = { viewModel.updateSearchQuery(it, SalesOrderSearchTypeEnum.SALES_ORDER_NUMBER) },
            placeholder = "주문번호로 검색",
            onSearch = { viewModel.search() },
        )

        // 리스트
        when (uiState) {
            is com.autoever.everp.utils.state.UiResult.Loading -> {
                Text(
                    text = "로딩 중...",
                    modifier = Modifier.padding(16.dp),
                )
            }

            is com.autoever.everp.utils.state.UiResult.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "오류가 발생했습니다: ${(uiState as com.autoever.everp.utils.state.UiResult.Error).exception.message}",
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Button(onClick = { viewModel.retry() }) {
                        Text("다시 시도")
                    }
                }
            }

            is com.autoever.everp.utils.state.UiResult.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(orderList) { order ->
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
                                    onClick = {
                                        navController.navigate(
                                            CustomerSubNavigationItem.SalesOrderDetailItem.createRoute(
                                                order.salesOrderId
                                            )
                                        )
                                    },
                                    modifier = Modifier.padding(top = 8.dp),
                                ) {
                                    Text("상세보기")
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    CustomerSubNavigationItem.SalesOrderDetailItem.createRoute(
                                        order.salesOrderId
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
