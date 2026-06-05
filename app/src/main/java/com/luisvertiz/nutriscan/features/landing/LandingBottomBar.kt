package com.luisvertiz.nutriscan.features.landing

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun LandingBottomBar(bottomBarNavController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        landingBottomBarItems.forEach { bottomBarItem ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(bottomBarItem.route::class) } == true

            NavigationBarItem(
                icon = { Icon(bottomBarItem.icon, contentDescription = null) },
                label = { Text(bottomBarItem.title) },
                selected = isSelected,
                enabled = bottomBarItem.isEnabled,
                onClick = {
                    bottomBarNavController.navigate(bottomBarItem.route) {
                        popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
