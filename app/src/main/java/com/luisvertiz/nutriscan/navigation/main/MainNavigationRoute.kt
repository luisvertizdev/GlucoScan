package com.luisvertiz.nutriscan.navigation.main

import kotlinx.serialization.Serializable
import com.luisvertiz.nutriscan.model.FoodAnalysisModel

sealed interface MainNavigationRoute {
    @Serializable
    data object Login : MainNavigationRoute

    @Serializable
    data object Register : MainNavigationRoute

    @Serializable
    data object NutritionGoal : MainNavigationRoute

    @Serializable
    data object NutritionResult : MainNavigationRoute

    @Serializable
    data object Dashboard : MainNavigationRoute

    @Serializable
    data object FoodCamera : MainNavigationRoute

    @Serializable
    data class FoodResult(
        val foodAnalysis: FoodAnalysisModel,
        val imagePath: String,
    ) : MainNavigationRoute

    @Serializable
    data object NutritionDetails : MainNavigationRoute
}