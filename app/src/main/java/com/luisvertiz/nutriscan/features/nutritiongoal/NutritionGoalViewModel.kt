package com.luisvertiz.nutriscan.features.nutritiongoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.GenderModel
import com.luisvertiz.nutriscan.model.GoalModel
import com.luisvertiz.nutriscan.model.NutritionGoalModel
import com.luisvertiz.nutriscan.model.NutritionResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NutritionGoalViewModel @Inject constructor(
    private val repository: NutritionGoalRepository
) : ViewModel() {

    private val _birthDate: MutableStateFlow<String> = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate

    private val _gender: MutableStateFlow<GenderModel?> = MutableStateFlow(null)
    val gender: StateFlow<GenderModel?> = _gender

    private val _activityLevel: MutableStateFlow<ActivityLevelModel?> = MutableStateFlow(null)
    val activityLevel: StateFlow<ActivityLevelModel?> = _activityLevel

    private val _mainGoalModel: MutableStateFlow<GoalModel?> = MutableStateFlow(null)
    val mainGoalModel: StateFlow<GoalModel?> = _mainGoalModel

    private val _weight: MutableStateFlow<String> = MutableStateFlow("")
    val weight: StateFlow<String> = _weight

    private val _height: MutableStateFlow<String> = MutableStateFlow("")
    val height: StateFlow<String> = _height

    private val _isEnabledCalculateGoalButton: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEnabledCalculateGoalButton: StateFlow<Boolean> = _isEnabledCalculateGoalButton

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _goToNutritionResult: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToNutritionResult: StateFlow<Boolean> = _goToNutritionResult

    fun setBirthDate(date: String) {
        viewModelScope.launch {
            _birthDate.value = date
            validateInputs()
        }
    }

    fun setGender(gender: GenderModel) {
        viewModelScope.launch {
            _gender.value = gender
            validateInputs()
        }
    }

    fun setActivityLevel(activityLevel: ActivityLevelModel) {
        viewModelScope.launch {
            _activityLevel.value = activityLevel
            validateInputs()
        }
    }

    fun setMainGoal(mainGoalModel: GoalModel) {
        viewModelScope.launch {
            _mainGoalModel.value = mainGoalModel
            validateInputs()
        }
    }

    fun setWeight(weight: String) {
        viewModelScope.launch {
            _weight.value = weight
            validateInputs()
        }
    }

    fun setHeight(height: String) {
        viewModelScope.launch {
            _height.value = height
            validateInputs()
        }
    }

    fun validateInputs() {
        viewModelScope.launch {
            val hasBirthdate: Boolean = _birthDate.value.isNotEmpty()
            val hasGender: Boolean = _gender.value != null
            val hasActivityLevel: Boolean = _activityLevel.value != null
            val hasWeight: Boolean = _weight.value.isNotEmpty()
            val hasHeight: Boolean = _height.value.isNotEmpty()
            val isEnabledLoginButton: Boolean = hasBirthdate && hasGender && hasActivityLevel && hasWeight && hasHeight
            _isEnabledCalculateGoalButton.value = isEnabledLoginButton
        }
    }

    // Formula de Mifflin-St Jeor.
    fun calculateNutritionGoal() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val tmb: Double = calculateTMB()
                val tdee: Double = calculateTDEE(tmb = tmb)
                val dailyCalories: Double = calculateDailyCalories(tdee = tdee)
                val dailyProteinGr: Double = calculateDailyProtein()
                val dailyFatGr: Double = calculateDailyFat()
                val dailyCarbsGr: Double = calculateDailyCarbs(
                    dailyCalories = dailyCalories,
                    proteinGr = dailyProteinGr,
                    fatGr = dailyFatGr
                )

                saveNutritionGoal(
                    dailyCalories = dailyCalories,
                    dailyProtein = dailyProteinGr,
                    dailyCarbs = dailyCarbsGr,
                    dailyFat = dailyFatGr
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Ocurrió un error inesperado"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // tasa metabolica basal
    private fun calculateTMB(): Double {
        val weightKg: Double = _weight.value.toDoubleOrNull() ?: 0.0
        val heightCm: Double = _height.value.toDoubleOrNull() ?: 0.0
        val age: Int = calculateAge()
        val genderNutritionFactor: Int = _gender.value?.nutritionFactor ?: 0

        val tmb: Double = (weightKg * 10 ) + (heightCm * 6.25) - (age * 5) + genderNutritionFactor
        return tmb
    }

    // tasa de calorias de mantenimiento
    private fun calculateTDEE(tmb: Double): Double {
        val levelActivityNutritionFactor: Double = _activityLevel.value?.nutritionFactor ?: 0.0
        return tmb * levelActivityNutritionFactor
    }

    private fun calculateDailyCalories(tdee: Double): Double {
        val mainGoalNutritionFactor: Double = _mainGoalModel.value?.nutritionFactor ?: 0.0
        val dailyCalories: Double = tdee * mainGoalNutritionFactor
        return dailyCalories
    }

    private fun calculateDailyProtein(): Double {
        val weightKg: Double = _weight.value.toDoubleOrNull() ?: 0.0
        val proteinFactor: Double = _mainGoalModel.value?.proteinFactor ?: 0.0
        return weightKg * proteinFactor
    }

    private fun calculateDailyFat(): Double {
        val weightKg: Double = _weight.value.toDoubleOrNull() ?: 0.0
        val fatFactor: Double = _mainGoalModel.value?.fatFactor ?: 0.0
        return weightKg * fatFactor
    }

    private fun calculateDailyCarbs(
        dailyCalories: Double,
        proteinGr: Double,
        fatGr: Double
    ): Double {
        val proteinCalories = proteinGr * 4
        val fatCalories = fatGr * 9
        val remainingCalories = dailyCalories - proteinCalories - fatCalories
        return remainingCalories / 4
    }

    private fun calculateAge(): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd / MM / yyyy")
            val birthDate = LocalDate.parse(_birthDate.value, formatter)
            Period.between(birthDate, LocalDate.now()).years
        } catch (exception: Exception) {
            0
        }
    }

    private fun saveNutritionGoal(
        dailyCalories: Double,
        dailyProtein: Double,
        dailyCarbs: Double,
        dailyFat: Double
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val nutritionGoalModel = NutritionGoalModel(
                    birthDate = _birthDate.value,
                    gender = _gender.value,
                    weightKg = _weight.value.toDoubleOrNull(),
                    heightCm = _height.value.toIntOrNull(),
                    activityLevel = _activityLevel.value,
                    goalModel = _mainGoalModel.value,
                    nutritionResult = NutritionResultModel(
                        dailyCalories = dailyCalories,
                        dailyProtein = dailyProtein,
                        dailyCarbs = dailyCarbs,
                        dailyFat = dailyFat,
                    ),
                )

                repository.saveNutritionGoal(nutritionGoalModel)
                _goToNutritionResult.value = true
            } catch (exception: Exception) {
                _errorMessage.value = exception.message
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun dismissError() = viewModelScope.launch {
        _errorMessage.value = ""
    }
}
