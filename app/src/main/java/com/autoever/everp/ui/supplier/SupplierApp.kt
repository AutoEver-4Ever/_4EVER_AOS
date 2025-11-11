package com.autoever.everp.ui.supplier

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.autoever.everp.ui.navigation.CustomNavigationBar

@Composable
fun SupplierApp(
    loginNavController: NavHostController
) {
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
                SupplierHomeScreen(navController = navController) // 공급업체 홈 화면
            }
            composable(SupplierNavigationItem.PurchaseOrder.route) {
                SupplierOrderScreen(navController = navController) // 발주 화면
            }
            composable(SupplierNavigationItem.Invoice.route) {
                SupplierVoucherScreen(navController = navController) // 전표 화면
            }
            composable(SupplierNavigationItem.Profile.route) {
                // 공통 프로필 화면을 호출할 수도 있음 (역할을 넘겨주거나 ViewModel 공유)
                SupplierProfileScreen(
                    loginNavController = loginNavController,
                    navController = navController
                ) // 공급업체 프로필 화면
            }
            // 서브 네비게이션 아이템들
            composable(
                route = SupplierSubNavigationItem.PurchaseOrderDetailItem.route,
            ) { backStackEntry ->
                val purchaseOrderId = backStackEntry.arguments
                    ?.getString(SupplierSubNavigationItem.PurchaseOrderDetailItem.ARG_ID)
                    ?: return@composable
                PurchaseOrderDetailScreen(navController = navController)
            }
            composable(
                route = SupplierSubNavigationItem.InvoiceDetailItem.route,
                arguments = listOf(
                    navArgument(SupplierSubNavigationItem.InvoiceDetailItem.ARG_ID) {
                        type = NavType.StringType
                    },
                    navArgument(SupplierSubNavigationItem.InvoiceDetailItem.ARG_IS_AP) {
                        type = NavType.BoolType
                        defaultValue = true
                    },
                ),
            ) { backStackEntry ->
                InvoiceDetailScreen(navController = navController)
            }
            composable(
                route = SupplierSubNavigationItem.ProfileEditItem.route,
            ) {
                SupplierProfileEditScreen(navController = navController)
            }
            composable(
                route = SupplierSubNavigationItem.NotificationItem.route,
            ) {
                NotificationScreen(navController = navController)
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
    SupplierApp(rememberNavController())
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
