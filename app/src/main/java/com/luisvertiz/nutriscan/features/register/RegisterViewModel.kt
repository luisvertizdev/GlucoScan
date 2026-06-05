package com.luisvertiz.nutriscan.features.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _fullName: MutableStateFlow<String> = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword: MutableStateFlow<String> = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _isEnabledRegisterButton: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEnabledRegisterButton: StateFlow<Boolean> = _isEnabledRegisterButton

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _successRegisterMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val successRegisterMessage: StateFlow<String?> = _successRegisterMessage

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isPasswordVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible

    private val _isConfirmPasswordVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConfirmPasswordVisible: StateFlow<Boolean> = _isConfirmPasswordVisible

    fun setFullName(fullName: String) {
        viewModelScope.launch {
            _fullName.value = fullName
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch {
            _email.value = email
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _password.value = password
        }
    }

    fun setConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _confirmPassword.value = confirmPassword
        }
    }

    fun validateInputs() {
        viewModelScope.launch {
            val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
            val isPasswordValid: Boolean = _password.value.length >= 8
            val isPasswordMatching: Boolean = _password.value == _confirmPassword.value
            _isEnabledRegisterButton.value = isEmailValid && isPasswordValid && isPasswordMatching
        }
    }

    fun register() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                registerRepository.register(_fullName.value,_email.value, _password.value)
                _successRegisterMessage.value = "Registro exitoso, verifica tu correo electrónico para poder iniciar sesión."
            } catch (exception: Exception) {
                _errorMessage.value = exception.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun dismissErrorDialog() = viewModelScope.launch {
        _errorMessage.value = null
    }

    fun dismissSuccessRegisterDialog() = viewModelScope.launch {
        _successRegisterMessage.value = null
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun toggleConfirmPasswordVisibility() {
        _isConfirmPasswordVisible.value = _isConfirmPasswordVisible.value.not()
    }
}
