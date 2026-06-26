package com.luisvertiz.nutriscan.features.foodresult

import com.luisvertiz.nutriscan.model.FoodAnalysisModel

data class UiState(
    val foodAnalysis: FoodAnalysisModel = FoodAnalysisModel(),
    val imagePath: String? = null,
    val isLoadingSaveMeal: Boolean = false,
    val idErrorMessage: Int? = null,
)