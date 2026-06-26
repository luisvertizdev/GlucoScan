package com.luisvertiz.nutriscan.features.profile

sealed class UiEffect {
    object GoToNutritionGoal : UiEffect()
    object GoToLogin : UiEffect()
}