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

    object Order : CustomerNavigationItem("customer_order", "주문", Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag)

    object Voucher : CustomerNavigationItem("customer_voucher", "전표", Icons.Outlined.Receipt, Icons.Filled.Receipt)

    object Profile : CustomerNavigationItem("customer_profile", "프로필", Icons.Outlined.Person, Icons.Filled.Person)

    companion object {
        val allDestinations = listOf(Home, Quotation, Order, Voucher, Profile)
    }
}
