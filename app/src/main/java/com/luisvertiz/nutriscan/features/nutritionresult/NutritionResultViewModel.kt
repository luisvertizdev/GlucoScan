package com.luisvertiz.nutriscan.features.nutritionresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.model.NutritionResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionResultViewModel @Inject constructor(
    private val nutritionResultRepository: NutritionResultRepository,
) : ViewModel() {


    private val _nutritionResult: MutableStateFlow<NutritionResultModel> = MutableStateFlow(NutritionResultModel())
    val nutritionResult: StateFlow<NutritionResultModel> = _nutritionResult

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

   init {
       getNutritionResult()
   }

    private fun getNutritionResult() {
        viewModelScope.launch {
            try {
                val nutritionResult: NutritionResultModel = nutritionResultRepository.getNutritionResult()
                _nutritionResult.value = nutritionResult
            } catch (exception: Exception) {
                _errorMessage.value = exception.message
            }
        }
    }

    fun dismissErrorDialog() {
        viewModelScope.launch {
            _errorMessage.value = null
        }
    }
}
