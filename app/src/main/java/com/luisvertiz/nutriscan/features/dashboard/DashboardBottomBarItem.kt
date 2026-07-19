package com.luisvertiz.nutriscan.features.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.navigation.bottombar.BottomBarNavigationRoute

data class LandingBottomBarItem(
    val title: Int,
    val icon: ImageVector,
    val route: Any,
    val isEnabled: Boolean
)

val dashboardBottomBarItems: List<LandingBottomBarItem> = listOf(
    LandingBottomBarItem(
        title = R.string.dashboard_bottom_bar_item_home,
        icon = Icons.Default.Home,
        route = BottomBarNavigationRoute.Home,
        isEnabled = true,
    ),
    LandingBottomBarItem(
        title = R.string.dashboard_bottom_bar_item_history,
        icon = Icons.Default.History,
        route = BottomBarNavigationRoute.History,
        isEnabled = true,
    ),
    LandingBottomBarItem(
        title = R.string.dashboard_bottom_bar_item_profile,
        icon = Icons.Default.Person,
        route = BottomBarNavigationRoute.Profile,
        isEnabled = true,
    )
)