package com.luisvertiz.nutriscan.features.history

import com.luisvertiz.nutriscan.model.MealModel

data class UiState(
    val groupedMeals: Map<String, List<MealModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val idErrorMessage: Int? = null
)
