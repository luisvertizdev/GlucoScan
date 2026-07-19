package com.luisvertiz.nutriscan.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.util.DateConstants.FORMAT_DD_MMMM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        getHistory()
    }

    fun getHistory() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val meals: List<MealModel> = repository.getAllMeals()
            val groupedMeals: Map<String, List<MealModel>> = groupMeals(meals)
            _uiState.update { it.copy(groupedMeals = groupedMeals) }
        } catch (e: Exception) {
            _uiState.update { it.copy(idErrorMessage = R.string.error_unknown) }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun groupMeals(meals: List<MealModel>): Map<String, List<MealModel>> {
        return meals.groupBy { meal ->
            SimpleDateFormat(FORMAT_DD_MMMM, Locale.getDefault()).format(Date(meal.timestamp))
        }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }
}
