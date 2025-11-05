package com.autoever.everp.ui.supplier

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoever.everp.ui.navigation.CustomNavigationBar

@Composable
fun SupplierApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { CustomNavigationBar(navController, SupplierNavigationItem.allDestinations) },
    ) { innerPadding ->
        // 2. 고객사용 NavHost
        NavHost(
            navController = navController,
            startDestination = SupplierNavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(SupplierNavigationItem.Home.route) {
                SupplierHomeScreen() // 공급업체 홈 화면
            }
            composable(SupplierNavigationItem.Order.route) {
                SupplierOrderScreen() // 주문 화면
            }
            composable(SupplierNavigationItem.Voucher.route) {
                SupplierVoucherScreen() // 전표 화면
            }
            composable(SupplierNavigationItem.Profile.route) {
                // 공통 프로필 화면을 호출할 수도 있음 (역할을 넘겨주거나 ViewModel 공유)
                SupplierProfileScreen() // 공급업체 프로필 화면
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SupplierAppPreview() {
    SupplierApp()
}

@Preview(showBackground = true)
@Composable
fun SupplierNavigationBarPreview() {
    val navController = rememberNavController()
    CustomNavigationBar(
        navController = navController,
        navItems = SupplierNavigationItem.allDestinations,
    )
}
