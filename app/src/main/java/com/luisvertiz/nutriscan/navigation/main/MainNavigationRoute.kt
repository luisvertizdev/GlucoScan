package com.luisvertiz.nutriscan.navigation.main

import kotlinx.serialization.Serializable

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
    data object Landing : MainNavigationRoute
}