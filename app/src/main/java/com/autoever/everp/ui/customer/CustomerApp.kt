package com.autoever.everp.ui.customer

import android.R.attr.defaultValue
import android.R.attr.type
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            // 고객사 메인 네비게이션 아이템들
            composable(CustomerNavigationItem.Home.route) {
                CustomerHomeScreen(navController = navController) // 고객사 홈 화면
            }
            composable(CustomerNavigationItem.Quotation.route) {
                CustomerQuotationScreen(navController = navController) // 견적 화면
            }
            composable(CustomerNavigationItem.SalesOrder.route) {
                CustomerOrderScreen(navController = navController) // 주문 화면
            }
            composable(CustomerNavigationItem.Invoice.route) {
                CustomerVoucherScreen(navController = navController) // 전표 화면
            }
            composable(CustomerNavigationItem.Profile.route) {
                // 공통 프로필 화면을 호출할 수도 있음 (역할을 넘겨주거나 ViewModel 공유)
                CustomerProfileScreen(
                    loginNavController = loginNavController,
                    navController = navController
                ) // 고객사 프로필 화면
            }
            // 고객사 서브 네비게이션 아이템들
            composable(CustomerSubNavigationItem.QuotationCreateItem.route) {
                QuotationCreateScreen(navController = navController)
            }
            composable(
                route = CustomerSubNavigationItem.QuotationDetailItem.route,
            ) { backStackEntry ->
                val quotationId = backStackEntry.arguments
                    ?.getString(CustomerSubNavigationItem.QuotationDetailItem.ARG_ID)
                    ?: return@composable
                QuotationDetailScreen(
                    navController = navController,
                    quotationId = quotationId,
                )
            }
            composable(
                route = CustomerSubNavigationItem.SalesOrderDetailItem.route,
            ) { backStackEntry ->
                val salesOrderId = backStackEntry.arguments
                    ?.getString(CustomerSubNavigationItem.SalesOrderDetailItem.ARG_ID)
                    ?: return@composable
                SalesOrderDetailScreen(
                    navController = navController,
                    salesOrderId = salesOrderId,
                )
            }
            composable(
                route = CustomerSubNavigationItem.InvoiceDetailItem.route,
                arguments = listOf(
                    navArgument(CustomerSubNavigationItem.InvoiceDetailItem.ARG_ID) {
                        type = NavType.StringType
                    },
                    navArgument(CustomerSubNavigationItem.InvoiceDetailItem.ARG_IS_AP) {
                        type = NavType.BoolType
                        defaultValue = false
                    },
                ),
            ) { backStackEntry ->
                val invoiceId = backStackEntry.arguments
                    ?.getString(CustomerSubNavigationItem.InvoiceDetailItem.ARG_ID)
                    ?: return@composable
                val isAp = backStackEntry.arguments
                    ?.getBoolean(CustomerSubNavigationItem.InvoiceDetailItem.ARG_IS_AP)
                    ?: false
                InvoiceDetailScreen(
                    navController = navController,
                    invoiceId = invoiceId,
                    isAp = isAp,
                )
            }
            composable(
                route = CustomerSubNavigationItem.ProfileEditItem.route,
            ) {
                CustomerProfileEditScreen(navController = navController)
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
