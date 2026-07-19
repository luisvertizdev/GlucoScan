package com.luisvertiz.nutriscan.features.nutritiondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.model.GlycemicImpactModel
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.util.NutritionConstants.LOW_GLYCEMIC_IMPACT_MAX
import com.luisvertiz.nutriscan.util.NutritionConstants.MEDIUM_GLYCEMIC_IMPACT_MAX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionDetailsViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val meals: List<MealModel> = repository.getTodayMeals()
            val nutritionGoal: NutritionResultModel = repository.getNutritionGoal()
            
            val totalCalories: Int = meals.sumOf { it.calories }
            val totalCarbs: Int = meals.sumOf { it.carbs }
            val avgImpact: String = calculateAverageImpact(meals)

            _uiState.update { 
                it.copy(
                    meals = meals,
                    nutritionGoal = nutritionGoal,
                    totalCalories = totalCalories,
                    totalCarbs = totalCarbs,
                    averageGlycemicImpact = avgImpact,
                    isLoading = false
                )
            }
        } catch (_: Exception) {
            _uiState.update { it.copy(isLoading = false, idErrorMessage = R.string.error_unknown) }
        }
    }

    private fun calculateAverageImpact(meals: List<MealModel>): String {
        if (meals.isEmpty()) return GlycemicImpactModel.LOW.description

        val average = meals.map {
            it.glycemicImpact?.impactScore ?: GlycemicImpactModel.LOW.impactScore
        }.average()

        return when {
            average < LOW_GLYCEMIC_IMPACT_MAX -> GlycemicImpactModel.LOW.description
            average < MEDIUM_GLYCEMIC_IMPACT_MAX -> GlycemicImpactModel.MEDIUM.description
            else -> GlycemicImpactModel.HIGH.description
        }
    }

    fun dismissErrorDialog() {
        _uiState.update { it.copy(idErrorMessage = null) }
    }
}
