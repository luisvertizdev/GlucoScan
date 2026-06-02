package com.luisvertiz.nutriscan.features.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isEnabledLoginButton: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEnabledLoginButton: StateFlow<Boolean> = _isEnabledLoginButton

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _goToHome: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goToHome: StateFlow<Boolean> = _goToHome

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
            val authResult: AuthResult = repository.login(_email.value, _password.value)
            val isSuccessfulLogin: Boolean = authResult.user != null

            if (isSuccessfulLogin) {
                _goToHome.value = true
            } else {
                _errorMessage.value = "No se pudo obtener la información de tu cuenta"
            }
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            _errorMessage.value = "El correo electrónico y/o la contraseña son incorrectos"
        } catch (_: FirebaseAuthInvalidUserException) {
            _errorMessage.value = "Tu cuenta no existe, regístrate"
        } finally {
            _isLoading.value = false
        }
    }

    fun dismissError() = viewModelScope.launch {
        _errorMessage.value = ""
    }
}
