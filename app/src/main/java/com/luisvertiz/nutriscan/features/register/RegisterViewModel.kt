package com.luisvertiz.nutriscan.features.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.R
import com.luisvertiz.nutriscan.error.ErrorHandler.EmailAlreadyRegisteredError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect: MutableSharedFlow<UiEffect> = MutableSharedFlow()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect

    fun setFullName(fullName: String) = viewModelScope.launch {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun setEmail(email: String) = viewModelScope.launch {
        _uiState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) = viewModelScope.launch {
        _uiState.update { it.copy(password = password) }
    }

    fun setConfirmPassword(confirmPassword: String) = viewModelScope.launch {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun validateInputs() = viewModelScope.launch {
        val email: String = _uiState.value.email
        val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        val password: String = _uiState.value.password
        val confirmPassword: String = _uiState.value.confirmPassword
        val isPasswordValid: Boolean = password.length >= 8 && confirmPassword.length >= 8
        val isPasswordMatching: Boolean = password == confirmPassword

        val isEnabledRegisterButton: Boolean = isEmailValid && isPasswordValid && isPasswordMatching
        _uiState.update { it.copy(isEnabledRegisterButton = isEnabledRegisterButton) }
    }

    fun register() = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }

            val fullName: String = _uiState.value.fullName
            val email: String = _uiState.value.email
            val password: String = _uiState.value.password
            registerRepository.register(
                fullName = fullName,
                email = email,
                password = password,
            )
            _uiState.update { it.copy(idSuccessRegisterMessage = R.string.success_register) }
        } catch (exception: Exception) {
            handleError(exception)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun handleError(exception: Exception) = viewModelScope.launch {
        val idErrorMessage = when (exception) {
            is EmailAlreadyRegisteredError -> R.string.error_email_already_registered
            else -> R.string.error_unknown
        }
        _uiState.update { it.copy(idErrorMessage = idErrorMessage) }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }

    fun dismissSuccessRegisterDialog() = viewModelScope.launch {
        _uiState.update { it.copy(idErrorMessage = null) }
    }

    fun togglePasswordVisibility() = viewModelScope.launch {
        val isPasswordVisible: Boolean = _uiState.value.isPasswordVisible
        _uiState.update { it.copy(isPasswordVisible = isPasswordVisible.not()) }
    }

    fun toggleConfirmPasswordVisibility() = viewModelScope.launch {
        val isConfirmPasswordVisible: Boolean = _uiState.value.isConfirmPasswordVisible
        _uiState.update { it.copy(isConfirmPasswordVisible = isConfirmPasswordVisible.not()) }
    }

    fun goBack() = viewModelScope.launch {
        _uiEffect.emit(value = UiEffect.GoBack)
    }

    fun goToLogin() = viewModelScope.launch {
        _uiEffect.emit(value = UiEffect.GoToLogin)
    }
}