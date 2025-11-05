package com.autoever.everp.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.ExperimentalFoundationApi
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.autoever.everp.ui.home.HomeViewModel
import com.autoever.everp.auth.session.AuthState
import com.autoever.everp.ui.navigation.Routes
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

private object TabRoutes {
    const val HOME = "tab_home"
    const val ORDERS = "tab_orders"
    const val QUOTES = "tab_quotes"
    const val VOUCHERS = "tab_vouchers"
    const val PROFILE = "tab_profile"
}

data class BottomNavItem(val route: String, val label: String)

@Composable
fun MainScreen(
    appNavController: NavController,
) {
    // Auth guard: if unauthenticated, navigate to LOGIN
    val homeVm: HomeViewModel = hiltViewModel()
    val authStateFlow = homeVm.authState
    LaunchedEffect(Unit) {
        authStateFlow
            .onEach { st ->
                if (st is AuthState.Unauthenticated) {
                    appNavController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            .collect()
    }
    val tabs = listOf(
        BottomNavItem(TabRoutes.HOME, "홈"),
        BottomNavItem(TabRoutes.ORDERS, "주문서"),
        BottomNavItem(TabRoutes.QUOTES, "견적서"),
        BottomNavItem(TabRoutes.VOUCHERS, "전표"),
        BottomNavItem(TabRoutes.PROFILE, "프로필"),
    )
    val tabNavController: NavHostController = rememberNavController()
    var selectedRoute by rememberSaveable { mutableStateOf(tabs.first().route) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { item ->
                    val selected = selectedRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            selectedRoute = item.route
                            if (tabNavController.currentDestination?.route != item.route) {
                                tabNavController.navigate(item.route) {
                                    popUpTo(tabNavController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Text(item.label.take(1)) },
                        label = { Text(item.label) },
                        alwaysShowLabel = true,
                    )
                }
            }
        }
    ) { padding ->
        TabNavHost(navController = tabNavController, appNavController = appNavController, modifier = Modifier.padding(padding))
    }
}

@Composable
private fun TabNavHost(navController: NavHostController, appNavController: NavController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = TabRoutes.HOME,
        modifier = modifier,
    ) {
        composable(TabRoutes.HOME) { HomeRootScreen(appNavController) }
        composable(TabRoutes.ORDERS) { OrdersRootScreen() }
        composable(TabRoutes.QUOTES) { QuotesRootScreen() }
        composable(TabRoutes.VOUCHERS) { VouchersRootScreen() }
        composable(TabRoutes.PROFILE) { ProfileRootScreen() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable private fun OrdersRootScreen() {
    val tabs = listOf("전체", "진행중", "완료")
    TopTabsPager(tabs = tabs) { page ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("주문서 - ${tabs[page]}")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable private fun QuotesRootScreen() {
    val tabs = listOf("전체", "요청", "승인")
    TopTabsPager(tabs = tabs) { page ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("견적서 - ${tabs[page]}")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable private fun VouchersRootScreen() {
    val tabs = listOf("매입", "매출", "일반분개")
    TopTabsPager(tabs = tabs) { page ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("전표 - ${tabs[page]}")
        }
    }
}

@Composable private fun ProfileRootScreen() {
    com.autoever.everp.ui.profile.ProfileScreen()
}

@Composable private fun HomeRootScreen(appNavController: NavController) {
    com.autoever.everp.ui.home.HomeScreen(navController = appNavController)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopTabsPager(
    tabs: List<String>,
    pageContent: @Composable (page: Int) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    TabRow(selectedTabIndex = pagerState.currentPage) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                text = { Text(title) },
            )
        }
    }
    HorizontalPager(state = pagerState) { page ->
        pageContent(page)
    }
}
