package com.luisvertiz.nutriscan.features.nutritiongoal

sealed class UiEffect {
    object GoBack : UiEffect()
    object GoToNutritionResult : UiEffect()
}