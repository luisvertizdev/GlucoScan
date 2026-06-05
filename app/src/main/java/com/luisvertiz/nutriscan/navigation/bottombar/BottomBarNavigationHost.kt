package com.luisvertiz.nutriscan.navigation.bottombar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luisvertiz.nutriscan.features.history.HistoryScreen
import com.luisvertiz.nutriscan.features.home.HomeScreen
import com.luisvertiz.nutriscan.features.profile.ProfileScreen

@Composable
fun BottomBarNavigationHost(
    mainNavController: NavHostController,
    bottomBarNavController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: BottomBarNavigationRoute = BottomBarNavigationRoute.Home
) {
    NavHost(
        navController = bottomBarNavController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<BottomBarNavigationRoute.Home> {
            HomeScreen(
                rootNavController = mainNavController
            )
        }

        composable<BottomBarNavigationRoute.History> {
            HistoryScreen()
        }

        composable<BottomBarNavigationRoute.Profile> {
            ProfileScreen()
        }
    }
}
