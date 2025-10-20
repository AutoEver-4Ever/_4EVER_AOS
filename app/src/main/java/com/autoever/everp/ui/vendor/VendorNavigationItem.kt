package com.autoever.everp.ui.vendor

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
import com.autoever.everp.ui.navigation.NavigationItem

sealed class VendorNavigationItem(
    override val route: String,
    override val label: String,
    override val outlinedIcon: ImageVector,
    override val filledIcon: ImageVector,
) : NavigationItem {
    object Home : VendorNavigationItem("vendor_home", "홈", Icons.Outlined.Home, Icons.Filled.Home)

    object Order : VendorNavigationItem("vendor_order", "주문", Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart)

    object Voucher : VendorNavigationItem("vendor_voucher", "전표", Icons.Outlined.Receipt, Icons.Filled.Receipt)

    object Profile : VendorNavigationItem("vendor_profile", "프로필", Icons.Outlined.Person, Icons.Filled.Person)

    companion object {
        val allDestinations = listOf(Home, Order, Voucher, Profile)
    }
}
