package com.autoever.everp.ui.customer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.RequestPage
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector
import com.autoever.everp.ui.navigation.NavigationItem

sealed class CustomerNavigationItem(
    override val route: String,
    override val label: String,
    override val outlinedIcon: ImageVector,
    override val filledIcon: ImageVector,
) : NavigationItem {
    object Home : CustomerNavigationItem("customer_home", "홈", Icons.Outlined.Home, Icons.Filled.Home)

    object Quotation :
        CustomerNavigationItem("customer_quotation", "견적", Icons.Outlined.RequestPage, Icons.Filled.RequestPage)

    object SalesOrder :
        CustomerNavigationItem("customer_sales_order", "주문", Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag)

    object Invoice : CustomerNavigationItem("customer_invoice", "전표", Icons.Outlined.Receipt, Icons.Filled.Receipt)

    object Profile : CustomerNavigationItem("customer_profile", "프로필", Icons.Outlined.Person, Icons.Filled.Person)

    companion object {
        val allDestinations = listOf(Home, Quotation, SalesOrder, Invoice, Profile)
    }
}

sealed class CustomerSubNavigationItem(
    val route: String,
    val label: String,
) {
    object QuotationCreateItem : CustomerSubNavigationItem("customer_quotation_create", "견적서 생성")

    object QuotationDetailItem :
        CustomerSubNavigationItem("customer_quotation_detail/{quotationId}", "견적서 상세") {

        const val ARG_ID = "quotationId"

        fun createRoute(quotationId: String): String {
            return "customer_quotation_detail/$quotationId"
        }
    }

    object SalesOrderDetailItem :
        CustomerSubNavigationItem("customer_order_detail/{salesOrderId}", "주문서 상세") {

        const val ARG_ID = "salesOrderId"

        fun createRoute(salesOrderId: String): String {
            return "customer_order_detail/$salesOrderId"
        }
    }

    object InvoiceDetailItem :
        CustomerSubNavigationItem("customer_invoice_detail/{invoiceId}?isAp={isAp}", "전표 상세") {

        const val ARG_ID = "invoiceId"
        const val ARG_IS_AP = "isAp"

        fun createRoute(invoiceId: String, isAp: Boolean = false): String {
            return "customer_invoice_detail/$invoiceId?isAp=$isAp"
        }
    }

    object ProfileEditItem : CustomerSubNavigationItem("customer_profile_edit", "프로필 수정")
}
