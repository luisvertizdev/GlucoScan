package com.luisvertiz.nutriscan.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import com.luisvertiz.nutriscan.features.dashboard.DashboardScreen
import com.luisvertiz.nutriscan.features.foodcamera.FoodCameraScreen
import com.luisvertiz.nutriscan.features.foodresult.FoodResultScreen
import com.luisvertiz.nutriscan.features.login.LoginScreen
import com.luisvertiz.nutriscan.features.nutritionresult.NutritionResultScreen
import com.luisvertiz.nutriscan.features.nutritiongoal.NutritionGoalScreen
import com.luisvertiz.nutriscan.features.register.RegisterScreen
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.navigation.navtype.FoodAnalysisNavType

@Composable
fun NavigationHost(
    mainNavController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: MainNavigationRoute,
) {
    NavHost(
        navController = mainNavController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<MainNavigationRoute.Login> {
            LoginScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.Register> {
            RegisterScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.NutritionGoal> {
            NutritionGoalScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.NutritionResult> {
            NutritionResultScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.Dashboard> {
            DashboardScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.FoodCamera> {
            FoodCameraScreen(
                mainNavController = mainNavController,
            )
        }

        composable<MainNavigationRoute.FoodResult>(
            typeMap = mapOf(
                typeOf<FoodAnalysisModel>() to FoodAnalysisNavType
            )
        ) { navBackStackEntry ->
            val foodResult = navBackStackEntry.toRoute<MainNavigationRoute.FoodResult>()
            FoodResultScreen(
                mainNavController = mainNavController,
                foodAnalysis = foodResult.foodAnalysis,
                imagePath = foodResult.imagePath,
            )
        }
    }
}
