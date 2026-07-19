package com.luisvertiz.nutriscan.features.foodresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodResultViewModel @Inject constructor(
    private val repository: FoodResultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    fun loadUiData(foodAnalysis: FoodAnalysisModel, imagePath: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                foodAnalysis = foodAnalysis,
                imagePath = imagePath,
            )
        }
    }

    fun saveMeal(foodAnalysis: FoodAnalysisModel, imagePath: String) = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoadingSaveMeal = true) }
            repository.saveMeal(foodAnalysis, imagePath)
            _uiEffect.emit(UiEffect.GoToDashboard)
        } catch (_: Exception) {
            _uiState.update { it.copy(idErrorMessage = R.string.error_unknown) }
        } finally {
            _uiState.update { it.copy(isLoadingSaveMeal = false) }
        }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }
}
