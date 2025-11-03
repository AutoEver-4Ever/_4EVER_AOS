package com.autoever.everp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoever.everp.auth.AuthState
import com.autoever.everp.ui.navigation.Routes
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.autoever.everp.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel(),
) {
    val stateFlow = vm.authState

    LaunchedEffect(Unit) {
        stateFlow
            .onEach { st ->
                if (st is AuthState.Unauthenticated) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            .collect()
    }
    Surface(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Header()

            UserInfoBanner()

            QuickActionsSection(onActionClick = { /* TODO: 목적지 네비게이션 연결 */ })

            RecentActivitiesSection()
        }
    }
}

// Header: 좌측 로고
@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.everp_logo),
            contentDescription = "EVERP 로고",
            modifier = Modifier.height(32.dp),
            contentScale = ContentScale.Fit,
            alignment = Alignment.CenterStart
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

// 사용자 배너: 간단 정보
@Composable
private fun UserInfoBanner() {
    EverCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x1A2196F3)) // 파란색 10% 투명
                    .padding(12.dp)
            ) {
                Text(
                    text = "👤",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "사용자 정보 로딩 중",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// 빠른 작업 섹션
@Composable
private fun QuickActionsSection(onActionClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "빠른 작업",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        // 2열 그리드 유사 배치(의존성 최소화): pair로 나눠 Row 2개 아이템
        val actions = listOf(
            QuickAction("견적 요청", "＋", Color(0xFF1976D2)),
            QuickAction("견적 목록", "🔍", Color(0xFF2E7D32)),
            QuickAction("주문 관리", "🛒", Color(0xFF6A1B9A)),
            QuickAction("매입전표", "🧾", Color(0xFFF57C00)),
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            actions.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowItems.forEach { action ->
                        EverCard(
                            modifier = Modifier.weight(1f),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(action.color)
                                        .height(48.dp)
                                        .fillMaxWidth(0.25f),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = action.symbol, color = Color.White)
                                }
                                Text(
                                    text = action.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                    if (rowItems.size == 1) {
                        // 홀수일 때 빈 공간 채우기
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// 최근 활동 섹션
@Composable
private fun RecentActivitiesSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "최근 활동",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val recent = listOf(
            RecentActivity(type = "견적", title = "Q2024-001 - 범퍼 견적서", date = "2024-01-15", status = "검토중"),
            RecentActivity(type = "주문", title = "O2024-005 - 사이드미러 주문", date = "2024-01-14", status = "배송중"),
            RecentActivity(type = "견적", title = "Q2024-002 - 헤드라이트 견적서", date = "2024-01-13", status = "승인됨"),
        )

        EverCard(modifier = Modifier.padding(horizontal = 16.dp)) {
            Column {
                recent.forEachIndexed { index, activity ->
                    ActivityRow(activity)
                    if (index != recent.lastIndex) {
                        Divider(modifier = Modifier.padding(start = 12.dp))
                    }
                }
            }
        }
    }
}

// 개별 최근 활동 행
@Composable
private fun ActivityRow(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TypeLabel(text = activity.type)
                StatusLabel(status = activity.status)
            }
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = activity.date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = ">",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// 라벨들
@Composable
private fun TypeLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .padding(horizontal = 8.dp, vertical = 5.dp),
    )
}

@Composable
private fun StatusLabel(status: String) {
    val colors = when (status) {
        "검토중" -> Color(0x33FFEB3B) to Color(0xFFFFEB3B) // Yellow bg/fg
        "배송중" -> Color(0x262196F3) to Color(0xFF2196F3) // Blue
        "승인됨" -> Color(0x2D4CAF50) to Color(0xFF4CAF50) // Green
        "거절됨" -> Color(0x2DF44336) to Color(0xFFF44336) // Red
        "만료됨" -> Color(0x2D9E9E9E) to Color(0xFF9E9E9E) // Gray
        else -> Color(0x2D4CAF50) to Color(0xFF4CAF50)
    }
    Text(
        text = status,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
        color = colors.second,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.first)
            .padding(horizontal = 8.dp, vertical = 5.dp),
    )
}

// 공통 카드
@Composable
private fun EverCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) { content() }
    }
}

// 모델들 (iOS와 유사)
private data class QuickAction(val title: String, val symbol: String, val color: Color)
private data class RecentActivity(val type: String, val title: String, val date: String, val status: String)
