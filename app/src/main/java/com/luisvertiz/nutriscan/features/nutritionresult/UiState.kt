package com.luisvertiz.nutriscan.features.nutritionresult

import androidx.annotation.StringRes
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.DiabetesTypeModel
import com.luisvertiz.nutriscan.model.GenderModel
import com.luisvertiz.nutriscan.model.NutritionResultModel

data class UiState(
    val nutritionResult: NutritionResultModel = NutritionResultModel(),
    val isLoading: Boolean = false,
    @StringRes val idErrorMessage: Int? = null,
)