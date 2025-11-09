package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
fun CustomerVoucherScreen(
    navController: NavController,
    viewModel: CustomerVoucherViewModel = hiltViewModel(),
) {
    val invoiceList by viewModel.invoiceList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedInvoiceIds by viewModel.selectedInvoiceIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // 헤더
        Text(
            text = "매입전표 관리",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
        )

        // 검색 바
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            placeholder = "전표번호, 내용, 거래처, 참조번호로 검색",
            onSearch = { viewModel.search() },
        )

        // 전체 선택 체크박스
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = selectedInvoiceIds.size == invoiceList.content.size && invoiceList.content.isNotEmpty(),
                onCheckedChange = {
                    if (it) {
                        viewModel.selectAll()
                    } else {
                        viewModel.clearSelection()
                    }
                },
            )
            Text(
                text = "전체 선택",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp),
            )
        }

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
                items(invoiceList.content) { invoice ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = selectedInvoiceIds.contains(invoice.id),
                            onCheckedChange = { viewModel.toggleInvoiceSelection(invoice.id) },
                            modifier = Modifier.padding(start = 8.dp),
                        )
                        ListCard(
                            id = invoice.number,
                            title = invoice.connection.name,
                            statusBadge = {
                                StatusBadge(
                                    text = invoice.status.displayName(),
                                    color = invoice.status.toColor(),
                                )
                            },
                            details = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "내용: ${invoice.connection.name}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    )
                                    Text(
                                        text = "거래처: ${invoice.connection.name}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    )
                                    Text(
                                        text = "금액: ${formatCurrency(invoice.totalAmount)}원",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = "전표 발생일: ${invoice.dueDate}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        text = "만기일: ${invoice.dueDate}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        text = "참조번호: ${invoice.reference.number}",
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    )
                                }
                            },
                            onClick = {
                                navController.navigate(
                                    CustomerSubNavigationItem.InvoiceDetailItem.createRoute(
                                        invoiceId = invoice.id
                                    ),
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

fun formatCurrency(amount: Long): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(amount)
}
