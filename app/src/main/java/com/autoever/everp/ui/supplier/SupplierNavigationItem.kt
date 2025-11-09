package com.autoever.everp.ui.supplier

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.autoever.everp.ui.customer.CustomerSubNavigationItem
import com.autoever.everp.ui.navigation.NavigationItem

sealed class SupplierNavigationItem(
    override val route: String,
    override val label: String,
    override val outlinedIcon: ImageVector,
    override val filledIcon: ImageVector,
) : NavigationItem {
    object Home : SupplierNavigationItem("supplier_home", "홈", Icons.Outlined.Home, Icons.Filled.Home)

    object PurchaseOrder :
        SupplierNavigationItem("supplier_purchase_order", "발주", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart)

    object Invoice : SupplierNavigationItem("supplier_invoice", "전표", Icons.Outlined.Receipt, Icons.Filled.Receipt)

    object Profile : SupplierNavigationItem("supplier_profile", "프로필", Icons.Outlined.Person, Icons.Filled.Person)

    companion object {
        val allDestinations = listOf(Home, PurchaseOrder, Invoice, Profile)
    }
}

sealed class SupplierSubNavigationItem(
    val route: String,
    val label: String,
) {
    object PurchaseOrderDetailItem :
        SupplierSubNavigationItem("supplier_purchase_order_detail/{purchaseOrderId}", "발주 상세") {

        const val ARG_ID = "purchaseOrderId"

        fun createRoute(purchaseOrderId: String): String {
            return "supplier_purchase_order_detail/$purchaseOrderId"
        }
    }

    object InvoiceDetailItem :
        SupplierSubNavigationItem("supplier_invoice_detail/{invoiceId}?isAp={isAp}", "전표 상세") {

        const val ARG_ID = "invoiceId"
        const val ARG_IS_AP = "isAp"

        fun createRoute(invoiceId: String, isAp: Boolean = true): String {
            return "supplier_invoice_detail/$invoiceId?isAp=$isAp"
        }
    }

    object ProfileEditItem : SupplierSubNavigationItem("supplier_profile_edit", "프로필 수정")
}
