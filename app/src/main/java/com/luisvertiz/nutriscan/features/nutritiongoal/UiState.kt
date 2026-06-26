package com.luisvertiz.nutriscan.features.nutritiongoal

import androidx.annotation.StringRes
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.DiabetesTypeModel
import com.luisvertiz.nutriscan.model.GenderModel

data class UiState(
    val birthDate: String = "",
    val showDatePicker: Boolean = false,
    val gender: GenderModel? = null,
    val isExpandedGenderDropdown: Boolean = false,
    val activityLevel: ActivityLevelModel? = null,
    val isExpandedActivityLevelDropdown: Boolean = false,
    val diabetesType: DiabetesTypeModel? = null,
    val isExpandedDiabetesTypeDropdown: Boolean = false,
    val weightKg: String = "",
    val heightCm: String = "",
    val isEnabledCalculateGoalButton: Boolean = false,
    val isLoading: Boolean = false,
    @StringRes val idErrorMessage: Int? = null,
)