package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.autoever.everp.ui.common.components.ListCard
import com.autoever.everp.ui.common.components.SearchBar
import com.autoever.everp.ui.common.components.StatusBadge
import com.autoever.everp.utils.state.UiResult
import java.time.format.DateTimeFormatter

@Composable
fun CustomerQuotationScreen(
    navController: NavController,
    viewModel: CustomerQuotationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val quotationList by viewModel.quotationList.collectAsStateWithLifecycle()
    val totalPage by viewModel.totalPages.collectAsStateWithLifecycle()
    val hasMore by viewModel.hasMore.collectAsStateWithLifecycle()
    val searchParams by viewModel.searchParams.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    // 무한 스크롤
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == totalPage - 1 && hasMore) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // 헤더
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "견적 관리",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Button(
                onClick = { navController.navigate(CustomerSubNavigationItem.QuotationCreateItem.route) },
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text("견적 요청")
            }
        }

        // 검색 바
        SearchBar(
            query = searchParams.search,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            placeholder = "견적번호, 고객명, 담당자로 검색",
            onSearch = { viewModel.search() },
        )

        // 리스트
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                // 초기 로딩
                uiState is UiResult.Loading && quotationList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // 에러 (리스트가 비어있을 때만)
                uiState is UiResult.Error && quotationList.isEmpty() -> {
                    val error = (uiState as UiResult.Error).exception
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("오류: ${error.message}")
                            Button(onClick = { viewModel.retry() }) {
                                Text("재시도")
                            }
                        }
                    }
                }

                // 리스트 표시
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(quotationList, key = { it.number }) { quotation ->
                            ListCard(
                                id = quotation.number,
                                title = "${quotation.customer.name} - ${quotation.product.productId}",
                                statusBadge = {
                                    StatusBadge(
                                        text = quotation.status.displayName(),
                                        color = quotation.status.toColor(),
                                    )
                                },
                                details = {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "고객명: ${quotation.customer.name}",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                        Text(
                                            text = "납기일: ${
                                                quotation.dueDate.format(
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                )
                                            }",
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                },
                                onClick = {
                                    navController.navigate(
                                        CustomerSubNavigationItem.QuotationDetailItem.createRoute(quotationId = quotation.id)
                                    )
                                },
                            )
                        }

                        // 페이지네이션 로딩
                        if (uiState is UiResult.Loading && quotationList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }

                        // 마지막 페이지 표시
                        if (!hasMore && quotationList.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "마지막 페이지입니다",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
