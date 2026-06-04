package com.luisvertiz.nutriscan.features.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isEnabledLoginButton: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEnabledLoginButton: StateFlow<Boolean> = _isEnabledLoginButton

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _goToNutritionSetup: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToNutritionSetup: StateFlow<Boolean> = _goToNutritionSetup

    private val _goToHome: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToHome: StateFlow<Boolean> = _goToHome

    private val _isPasswordVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    fun setEmail(email: String) = viewModelScope.launch {
        _email.value = email
    }

    fun setPassword(password: String) = viewModelScope.launch {
        _password.value = password
    }

    fun validateInputs() = viewModelScope.launch {
        val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        val isPasswordValid: Boolean = _password.value.length >= 8
        _isEnabledLoginButton.value = isEmailValid && isPasswordValid
    }

    fun login() = viewModelScope.launch {
        try {
            _isLoading.value = true
            loginRepository.login(_email.value, _password.value)
            _goToNutritionSetup.value = true
        } catch (exception: Exception) {
            _errorMessage.value = exception.message
        } finally {
            _isLoading.value = false
        }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _errorMessage.value = null
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = _isPasswordVisible.value.not()
    }
}
