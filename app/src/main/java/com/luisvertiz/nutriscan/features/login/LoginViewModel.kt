package com.luisvertiz.nutriscan.features.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler.EmailNotVerifiedError
import com.luisvertiz.nutriscan.error.ErrorHandler.InvalidCredentialsError
import com.luisvertiz.nutriscan.error.ErrorHandler.InvalidUserError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    fun setEmail(email: String) = viewModelScope.launch {
        _uiState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) = viewModelScope.launch {
        _uiState.update { it.copy(password = password) }
    }

    fun validateInputs() = viewModelScope.launch {
        val email: String = _uiState.value.email
        val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val password: String = _uiState.value.password
        val isPasswordValid: Boolean = password.length >= 8

        val isEnabledLoginButton: Boolean = isEmailValid && isPasswordValid
        _uiState.update { it.copy(isEnabledLoginButton = isEnabledLoginButton) }
    }

    fun login() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val email: String = _uiState.value.email
            val password: String = _uiState.value.password
            loginRepository.login(
                email = email,
                password = password,
            )
            _uiEffect.emit(value = UiEffect.GoToDashboard)
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun handleError(exception: Exception) = viewModelScope.launch {
        val idErrorMessage = when (exception) {
            is EmailNotVerifiedError -> R.string.error_email_not_verified
            is InvalidUserError -> R.string.error_invalid_user
            is InvalidCredentialsError -> R.string.error_invalid_credentials
            else -> R.string.error_unknown
        }
        _uiState.update { it.copy(idErrorMessage = idErrorMessage) }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }

    fun togglePasswordVisibility() = viewModelScope.launch {
        val isPasswordVisible: Boolean = _uiState.value.isPasswordVisible
        _uiState.update { it.copy(isPasswordVisible = isPasswordVisible.not()) }
    }

    fun goToRegister() = viewModelScope.launch {
        _uiEffect.emit(value = UiEffect.GoToRegister)
    }
}