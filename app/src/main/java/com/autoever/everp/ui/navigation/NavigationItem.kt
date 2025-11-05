package com.autoever.everp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface NavigationItem {
    val route: String
    val label: String
    val outlinedIcon: ImageVector
    val filledIcon: ImageVector
}
