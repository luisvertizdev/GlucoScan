package com.luisvertiz.nutriscan.features.landing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.luisvertiz.nutriscan.navigation.bottombar.BottomBarNavigationRoute

data class LandingBottomBarItem(
    val title: String,
    val icon: ImageVector,
    val route: Any,
    val isEnabled: Boolean
)

val landingBottomBarItems: List<LandingBottomBarItem> = listOf(
    LandingBottomBarItem(
        title = "Inicio",
        icon = Icons.Default.Home,
        route = BottomBarNavigationRoute.Home,
        isEnabled = true
    ),
    LandingBottomBarItem(
        title = "Historial",
        icon = Icons.Default.History,
        route = BottomBarNavigationRoute.History,
        isEnabled = false
    ),
    LandingBottomBarItem(
        title = "Perfil",
        icon = Icons.Default.Person,
        route = BottomBarNavigationRoute.Profile,
        isEnabled = false
    )
)
