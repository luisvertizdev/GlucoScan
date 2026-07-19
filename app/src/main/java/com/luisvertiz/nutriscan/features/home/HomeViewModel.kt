package com.luisvertiz.nutriscan.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val user: UserModel = homeRepository.getUser()
            val todayDate: String = DateUtil.formatHeaderDate(System.currentTimeMillis())
            
            if (user.nutritionGoal == null) {
                _uiState.update { it.copy(user = user, todayDate = todayDate) }
            } else {
                val todayMeals: List<MealModel> = homeRepository.getTodayMeals()
                val totalConsumedCarbs: Int = todayMeals.sumOf { it.carbs }

                _uiState.update { 
                    it.copy(
                        user = user, 
                        totalConsumedCarbs = totalConsumedCarbs,
                        todayDate = todayDate,
                    ) 
                }
            }
            
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
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

    fun goToNutritionGoal() = viewModelScope.launch {
        _uiEffect.emit(value = UiEffect.GoToNutritionGoal)
    }

    fun goToFoodCamera() = viewModelScope.launch {
        _uiEffect.emit(value = UiEffect.GoToFoodCamera)
    }
}