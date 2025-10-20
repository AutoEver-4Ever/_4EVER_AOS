package com.autoever.everp.ui.vendor

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
fun VendorApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { CustomNavigationBar(navController, VendorNavigationItem.allDestinations) },
    ) { innerPadding ->
        // 2. 고객사용 NavHost
        NavHost(
            navController = navController,
            startDestination = VendorNavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(VendorNavigationItem.Home.route) {
                VendorHomeScreen() // 공급업체 홈 화면
            }
            composable(VendorNavigationItem.Order.route) {
                VendorOrderScreen() // 주문 화면
            }
            composable(VendorNavigationItem.Voucher.route) {
                VendorVoucherScreen() // 전표 화면
            }
            composable(VendorNavigationItem.Profile.route) {
                // 공통 프로필 화면을 호출할 수도 있음 (역할을 넘겨주거나 ViewModel 공유)
                VendorProfileScreen() // 공급업체 프로필 화면
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun VendorAppPreview() {
    VendorApp()
}

@Preview(showBackground = true)
@Composable
fun VendorBottomBarPreview() {
    val navController = rememberNavController()
    CustomNavigationBar(
        navController = navController,
        navItems = VendorNavigationItem.allDestinations,
    )
}
