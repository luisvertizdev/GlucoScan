package com.luisvertiz.nutriscan.features.foodcamera

import com.luisvertiz.nutriscan.model.FoodAnalysisModel

sealed class UiEffect {
    data class GoToFoodResult(
        val foodAnalysis: FoodAnalysisModel,
        val imagePath: String,
    ) : UiEffect()
}