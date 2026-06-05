package com.luisvertiz.nutriscan.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luisvertiz.nutriscan.features.landing.LandingScreen
import com.luisvertiz.nutriscan.features.login.LoginScreen
import com.luisvertiz.nutriscan.features.nutritionresult.NutritionResultScreen
import com.luisvertiz.nutriscan.features.nutritiongoal.NutritionGoalScreen
import com.luisvertiz.nutriscan.features.register.RegisterScreen

@Composable
fun NavigationHost(
    mainNavController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: MainNavigationRoute = MainNavigationRoute.Login
) {
    NavHost(
        navController = mainNavController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<MainNavigationRoute.Login> {
            LoginScreen(
                mainNavController = mainNavController
            )
        }

        composable<MainNavigationRoute.Register> {
            RegisterScreen(
                mainNavController = mainNavController
            )
        }

        composable<MainNavigationRoute.NutritionGoal> {
            NutritionGoalScreen(
                mainNavController = mainNavController
            )
        }

        composable<MainNavigationRoute.NutritionResult> {
            NutritionResultScreen(
                mainNavController = mainNavController
            )
        }

        composable<MainNavigationRoute.Landing> {
            LandingScreen(
                mainNavController = mainNavController
            )
        }
    }
}
