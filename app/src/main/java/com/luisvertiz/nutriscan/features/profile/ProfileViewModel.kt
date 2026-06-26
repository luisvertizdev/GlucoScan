package com.luisvertiz.nutriscan.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler
import com.luisvertiz.nutriscan.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    init {
        getUser()
    }

    private fun getUser() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isScreenLoading = true) }
            val user: UserModel = profileRepository.getUser()
            _uiState.update { it.copy(user = user) }
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isScreenLoading = false) }
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

    fun logout() = viewModelScope.launch {
        _uiState.update { it.copy(isLogoutButtonLoading = true) }
        profileRepository.logout()
        _uiState.update { it.copy(isLogoutButtonLoading = false) }
        _uiEffect.emit(UiEffect.GoToLogin)
    }

    fun goToNutritionGoal() = viewModelScope.launch {
        _uiEffect.emit(UiEffect.GoToNutritionGoal)
    }
}