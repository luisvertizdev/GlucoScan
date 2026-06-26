package com.luisvertiz.nutriscan.features.nutritiongoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.ActivityLevelModel
import com.luisvertiz.nutriscan.model.GenderModel
import com.luisvertiz.nutriscan.model.DiabetesTypeModel
import com.luisvertiz.nutriscan.model.NutritionGoalModel
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.util.NutritionConstants.AGE_FACTOR
import com.luisvertiz.nutriscan.util.NutritionConstants.HEIGHT_FACTOR
import com.luisvertiz.nutriscan.util.NutritionConstants.WEIGHT_FACTOR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class NutritionGoalViewModel @Inject constructor(
    private val repository: NutritionGoalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    fun setBirthDate(dateMillis: Long) = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.format(Date(dateMillis))
        _uiState.update { it.copy(birthDate = date) }
        validateInputs()
    }

    fun setBirthDate(date: String) = viewModelScope.launch {
        _uiState.update { it.copy(birthDate = date) }
        validateInputs()
    }

    fun setGender(gender: GenderModel) = viewModelScope.launch {
        _uiState.update { it.copy(gender = gender) }
        validateInputs()
    }

    fun setActivityLevel(activityLevel: ActivityLevelModel) = viewModelScope.launch {
        _uiState.update { it.copy(activityLevel = activityLevel) }
        validateInputs()
    }

    fun setDiabetesType(diabetesType: DiabetesTypeModel) = viewModelScope.launch {
        _uiState.update { it.copy(diabetesType = diabetesType) }
        validateInputs()
    }

    fun setWeightKg(weightKg: String) = viewModelScope.launch {
        _uiState.update { it.copy(weightKg = weightKg) }
        validateInputs()
    }

    fun setHeightCm(heightCm: String) = viewModelScope.launch {
        _uiState.update { it.copy(heightCm = heightCm) }
        validateInputs()
    }

    fun validateInputs() {
        viewModelScope.launch {
            val hasBirthdate: Boolean = _uiState.value.birthDate.isNotEmpty()
            val hasGender: Boolean = _uiState.value.gender != null
            val hasActivityLevel: Boolean = _uiState.value.activityLevel != null
            val hasWeight: Boolean = _uiState.value.weightKg.isNotEmpty()
            val hasHeight: Boolean = _uiState.value.heightCm.isNotEmpty()
            val isEnabledCalculateGoalButton: Boolean = hasBirthdate && hasGender && hasActivityLevel && hasWeight && hasHeight
            _uiState.update { it.copy(isEnabledCalculateGoalButton = isEnabledCalculateGoalButton) }
        }
    }

    // Formula de Mifflin-St Jeor.
    fun calculateNutritionGoal() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val age: Int = calculateAge()
        val bmr: Double = calculateBMR(age)
        val dailyCalories: Double = calculateTDEE(bmr)
        val dailyCarbsGr: Int = calculateDailyCarbs(dailyCalories)

        saveNutritionGoal(
            dailyCalories = dailyCalories,
            dailyCarbsGr = dailyCarbsGr,
        )
    }

    // tasa metabolica basal
    private fun calculateBMR(age: Int): Double {
        val weightKg: Double = _uiState.value.weightKg.toDoubleOrNull() ?: 0.0
        val heightCm: Double = _uiState.value.heightCm.toDoubleOrNull() ?: 0.0
        val genderBMROffset: Int = _uiState.value.gender?.bmrOffset ?: 0

        val tmb: Double = (weightKg * WEIGHT_FACTOR) + (heightCm * HEIGHT_FACTOR) - (age * AGE_FACTOR) + genderBMROffset
        return tmb
    }

    // tasa de calorias de mantenimiento
    private fun calculateTDEE(bmr: Double): Double {
        val levelActivityNutritionFactor: Double = _uiState.value.activityLevel?.nutritionFactor ?: 0.0
        return bmr * levelActivityNutritionFactor
    }

    private fun calculateDailyCarbs(dailyCalories: Double): Int {
        val dailyCarbs: Int = ((dailyCalories * 0.4) / 4.0).toInt()
        return dailyCarbs
    }

    private fun calculateAge(): Int {
        return try {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd / MM / yyyy")
            val birthDate: LocalDate = LocalDate.parse(_uiState.value.birthDate, formatter)
            Period.between(birthDate, LocalDate.now()).years
        } catch (_: Exception) {
            0
        }
    }

    private fun saveNutritionGoal(dailyCalories: Double, dailyCarbsGr: Int) = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }

            val nutritionGoalModel = NutritionGoalModel(
                birthDate = _uiState.value.birthDate,
                gender = _uiState.value.gender,
                weightKg = _uiState.value.weightKg.toDoubleOrNull(),
                heightCm = _uiState.value.heightCm.toIntOrNull(),
                activityLevel = _uiState.value.activityLevel,
                diabetesTypeModel = _uiState.value.diabetesType,
                nutritionResult = NutritionResultModel(
                    dailyCalories = dailyCalories,
                    dailyCarbsGr = dailyCarbsGr,
                ),
            )

            repository.saveNutritionGoal(nutritionGoalModel)
            _uiEffect.emit(value = UiEffect.GoToNutritionResult)
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun handleError(exception: Exception) = viewModelScope.launch {
        val idErrorMessage = when (exception) {
            is UnknownError -> R.string.error_unknown
            else -> R.string.error_unknown
        }
        _uiState.update { it.copy(idErrorMessage = idErrorMessage) }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }

    fun showDatePicker(show: Boolean) = viewModelScope.launch {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun toggleGenderDropdown() = viewModelScope.launch {
        val isExpandedGenderDropdown: Boolean = _uiState.value.isExpandedGenderDropdown
        _uiState.update { it.copy(isExpandedGenderDropdown = isExpandedGenderDropdown.not()) }
    }

    fun toggleActivityLevelDropdown() = viewModelScope.launch {
        val isExpandedActivityLevelDropdown: Boolean = _uiState.value.isExpandedActivityLevelDropdown
        _uiState.update { it.copy(isExpandedActivityLevelDropdown = isExpandedActivityLevelDropdown.not()) }
    }

    fun toggleDiabetesTypeDropdown() = viewModelScope.launch {
        val isExpandedDiabetesTypeDropdown: Boolean = _uiState.value.isExpandedDiabetesTypeDropdown
        _uiState.update { it.copy(isExpandedDiabetesTypeDropdown = isExpandedDiabetesTypeDropdown.not()) }
    }
}