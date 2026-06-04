package com.luisvertiz.nutriscan.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable
    data object Login : NavigationRoute

    @Serializable
    data object Register : NavigationRoute

    @Serializable
    data object NutritionSetup : NavigationRoute

    @Serializable
    data object NutritionGoal : NavigationRoute

    @Serializable
    data object Home : NavigationRoute
}

