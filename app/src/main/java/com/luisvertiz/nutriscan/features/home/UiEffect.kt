package com.luisvertiz.nutriscan.features.home

sealed class UiEffect {
    object GoToNutritionGoal : UiEffect()
    object GoToFoodCamera : UiEffect()
}