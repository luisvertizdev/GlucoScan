package com.luisvertiz.nutriscan.features.nutritiondetails

import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.model.NutritionResultModel

data class UiState(
    val isLoading: Boolean = false,
    val meals: List<MealModel> = emptyList(),
    val nutritionGoal: NutritionResultModel? = null,
    val totalCalories: Int = 0,
    val totalCarbs: Int = 0,
    val averageGlycemicImpact: String = "LOW",
    val idErrorMessage: Int? = null
)
