package com.luisvertiz.nutriscan.features.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luisvertiz.nutriscan.navigation.bottombar.BottomBarNavigationHost
import com.luisvertiz.nutriscan.navigation.bottombar.BottomBarNavigationRoute

@Composable
fun DashboardScreen(
    mainNavController: NavHostController,
) {
    val bottomBarNavController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            DashboardBottomBar(
                bottomBarNavController = bottomBarNavController,
            )
        },
        content = { contentPadding ->
            BottomBarNavigationHost(
                mainNavController = mainNavController,
                bottomBarNavController = bottomBarNavController,
                modifier = Modifier.padding(contentPadding),
                startDestination = BottomBarNavigationRoute.Home,
            )
        }
    )
}