package com.autoever.everp.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.home.HomeScreen
import com.autoever.everp.ui.login.LoginScreen

object Routes {
    const val HOME = "home"
    const val LOGIN = "login"
}

/**
 * 앱 네비게이션 그래프.
 * 현재는 화면 구현 전이므로 간단한 플레이스홀더를 렌더링합니다.
 *  - startDestination: home
 *  - routes: home, login
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(route = Routes.HOME) { HomeScreen(navController = navController) }
        composable(route = Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    // TODO: AppAuth 인가 플로우 시작 연결 예정
                }
            )
        }
    }
}
