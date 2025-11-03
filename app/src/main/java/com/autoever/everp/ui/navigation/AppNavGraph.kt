package com.autoever.everp.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.home.HomeScreen
import com.autoever.everp.ui.login.LoginScreen
import com.autoever.everp.ui.auth.AuthWebViewScreen
import com.autoever.everp.ui.auth.AuthExchangeViewModel
import androidx.hilt.navigation.compose.hiltViewModel

object Routes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val AUTH_WEB = "authweb"
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
                onLoginClick = { navController.navigate(Routes.AUTH_WEB) }
            )
        }
        composable(route = Routes.AUTH_WEB) {
            val exchangeVm: AuthExchangeViewModel = hiltViewModel()
            AuthWebViewScreen(
                onCode = { code, codeVerifier ->
                    exchangeVm.exchange(
                        code = code,
                        verifier = codeVerifier,
                        onSuccess = {
                            // 인증 완료 → 홈으로 이동하며 스택 정리
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onError = {
                            // 에러 시 웹뷰 유지(사용자 재시도 가능)
                        }
                    )
                }
            )
        }
    }
}
