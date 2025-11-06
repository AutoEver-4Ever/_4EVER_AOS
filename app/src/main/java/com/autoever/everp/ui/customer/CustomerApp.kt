package com.autoever.everp.ui.customer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.navigation.CustomNavigationBar

@Composable
fun CustomerApp(
    loginNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { CustomNavigationBar(navController, CustomerNavigationItem.allDestinations) },
    ) { innerPadding ->
        // 2. 고객사용 NavHost
        NavHost(
            navController = navController,
            startDestination = CustomerNavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(CustomerNavigationItem.Home.route) {
                CustomerHomeScreen(navController = navController) // 고객사 홈 화면
            }
            composable(CustomerNavigationItem.Quotation.route) {
                CustomerQuotationScreen(navController = navController) // 견적 화면
            }
            composable(CustomerNavigationItem.Order.route) {
                CustomerOrderScreen(navController = navController) // 주문 화면
            }
            composable(CustomerNavigationItem.Voucher.route) {
                CustomerVoucherScreen(navController = navController) // 전표 화면
            }
            composable(CustomerNavigationItem.Profile.route) {
                // 공통 프로필 화면을 호출할 수도 있음 (역할을 넘겨주거나 ViewModel 공유)
                CustomerProfileScreen(navController = navController) // 고객사 프로필 화면
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun CustomerAppPreview() {
    CustomerApp(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun CustomerBottomBarPreview() {
    val navController = rememberNavController()
    CustomNavigationBar(
        navController = navController,
        navItems = CustomerNavigationItem.allDestinations,
    )
}
