package com.autoever.everp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.home.HomeScreen
import com.autoever.everp.ui.login.LoginScreen
import androidx.compose.ui.platform.LocalContext

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
            val ctx = LocalContext.current
            LoginScreen(
                onLoginClick = { com.autoever.everp.auth.AuthCct.start(ctx) }
            )
        }
        
    }
}
