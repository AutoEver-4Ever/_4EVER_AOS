package com.autoever.everp.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.home.HomeScreen
import com.autoever.everp.ui.login.LoginScreen
import androidx.compose.ui.platform.LocalContext
import com.autoever.everp.auth.flow.AuthCct
import androidx.hilt.navigation.compose.hiltViewModel
import com.autoever.everp.ui.home.HomeViewModel
import com.autoever.everp.auth.session.AuthState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect

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
            val homeVm: HomeViewModel = hiltViewModel()
            val stateFlow = homeVm.authState
            LaunchedEffect(Unit) {
                stateFlow
                    .onEach { st ->
                        if (st is AuthState.Authenticated) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                    .collect()
            }
            LoginScreen(
                onLoginClick = {
                    Log.i("AuthFlow", "[INFO] 로그인 버튼 클릭")
                    AuthCct.start(ctx) }
            )
        }

    }
}
