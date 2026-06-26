package com.luisvertiz.nutriscan.features.nutritionresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler
import com.luisvertiz.nutriscan.model.NutritionResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionResultViewModel @Inject constructor(
    private val nutritionResultRepository: NutritionResultRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

   init {
       getNutritionResult()
   }

    private fun getNutritionResult() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val nutritionResult: NutritionResultModel = nutritionResultRepository.getNutritionResult()
            _uiState.update { it.copy(nutritionResult = nutritionResult) }
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun goBack() = viewModelScope.launch {
        _uiEffect.emit(UiEffect.GoBack)
    }

    private fun handleError(exception: Exception) = viewModelScope.launch {
        val idErrorMessage = when (exception) {
            is ErrorHandler.UnknownError -> R.string.error_unknown
            else -> R.string.error_unknown
        }
        _uiState.update { it.copy(idErrorMessage = idErrorMessage) }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }
}
