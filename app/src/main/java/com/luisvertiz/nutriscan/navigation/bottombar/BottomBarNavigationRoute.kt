package com.luisvertiz.nutriscan.navigation.bottombar

import kotlinx.serialization.Serializable

sealed interface BottomBarNavigationRoute {
    @Serializable
    data object Home : BottomBarNavigationRoute

    @Serializable
    data object History : BottomBarNavigationRoute

    @Serializable
    data object Profile : BottomBarNavigationRoute
}