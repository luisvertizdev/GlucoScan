package com.luisvertiz.nutriscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luisvertiz.nutriscan.features.login.LoginScreen
import com.luisvertiz.nutriscan.features.nutritiongoal.NutritionGoalScreen
import com.luisvertiz.nutriscan.features.nutritionsetup.NutritionSetupScreen
import com.luisvertiz.nutriscan.features.register.RegisterScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: NavigationRoute = NavigationRoute.Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<NavigationRoute.Login> {
            LoginScreen(
                navController = navController
            )
        }

        composable<NavigationRoute.Register> {
            RegisterScreen(
                navController = navController
            )
        }

        composable<NavigationRoute.NutritionSetup> {
            NutritionSetupScreen(
                navController = navController
            )
        }

        composable<NavigationRoute.NutritionGoal> {
            NutritionGoalScreen(
                navController = navController
            )
        }
    }
}
