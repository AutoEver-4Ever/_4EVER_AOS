package com.autoever.everp.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.autoever.everp.ui.navigation.NavigationItem
import kotlin.collections.forEach

@Composable
fun CustomNavigationBar(
    navController: NavHostController,
    navItems: List<NavigationItem>,
) {
    // 1. NavigationBar (BottomNavigationView의 Compose 버전)
    NavigationBar {
        // 2. 현재 NavController의 백 스택 항목을 State로 관찰
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // 3. 현재 화면의 '목적지(Destination)'
        val currentDestination = navBackStackEntry?.destination

        // 4. 1단계에서 만든 스크린 리스트를 순회하며 탭 아이템 생성
        navItems.forEach { screen ->

            // (3) 이 탭이 현재 선택되었는지 여부를 변수로 저장
            // 6. (중요) 현재 선택된 탭인지 확인
            // 현재 목적지의 경로 계층(hierarchy)에
            // 이 탭의 경로(route)가 포함되어 있는지 확인합니다.
            // (단순 == 비교보다 '홈 > 홈 상세'처럼 중첩된 경우에도 '홈' 탭이 활성화되어 더 좋습니다.)
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            // 5. NavigationBarItem (각 탭 아이템)
            NavigationBarItem(
                selected = isSelected, // (4) selected 상태 전달
                onClick = {
                    if (isSelected) {
                        // (A) Behavior 2: 이미 선택된 탭을 또 누름
                        // 이 탭의 루트 스크린으로 이동하고, 그 위의 모든 스택을 날림
                        navController.popBackStack(screen.route, inclusive = false)
                    } else {
                        // (B) Behavior 1: 다른 탭을 누름
                        // 상태를 저장/복원하며 탭 이동
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true // 현재 탭의 백 스택 저장
                            }
                            launchSingleTop = true // 중복 화면 방지
                            restoreState = true // 이동할 탭의 백 스택 복원
                        }
                    }
                },
                // (5) ⭐ 아이콘 동적 변경
                // TODO 아이콘을 ImageVector로 변경
                icon = {
                    if (isSelected) {
                        Icon(
                            imageVector = screen.filledIcon,
                            contentDescription = screen.label,
                        )
                    } else {
                        Icon(
                            imageVector = screen.outlinedIcon,
                            contentDescription = screen.label,
                        )
                    }
                },
                label = { Text(screen.label) },
                // (6) ⭐ 색상 동적 변경
                // TODO NavigationBarItemDefaults.colors()를 사용하여 선택/비선택 상태에 따른 아이콘 및 라벨 색상을 지정
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary, // 선택된 아이콘 색상
                        selectedTextColor = MaterialTheme.colorScheme.primary, // 선택된 라벨 색상
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, // 선택 안 된 아이콘 색상
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant, // 선택 안 된 라벨 색상
                        // indicatorColor = ... // 탭 배경 하이라이트 색상 (선택적)
                    ),
            )
        }
    }
}
