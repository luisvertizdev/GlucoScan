package com.luisvertiz.nutriscan.features.nutritionsetup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisvertiz.nutriscan.features.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionSetupViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _birthDate: MutableStateFlow<String> = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate

    private val _gender: MutableStateFlow<String> = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _activityLevel: MutableStateFlow<String> = MutableStateFlow("")
    val activityLevel: StateFlow<String> = _activityLevel

    private val _mainGoal: MutableStateFlow<String> = MutableStateFlow("")
    val mainGoal: StateFlow<String> = _mainGoal

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

    fun setBirthDate(date: String) = viewModelScope.launch {
        _birthDate.value = date
    }

    fun setGender(gender: String) = viewModelScope.launch {
        _gender.value = gender
    }

    fun setActivityLevel(activityLevel: String) = viewModelScope.launch {
        _activityLevel.value = activityLevel
    }

    fun setMainGoal(mainGoal: String) = viewModelScope.launch {
        _mainGoal.value = mainGoal
    }

    fun validateInputs() = viewModelScope.launch {
        val isEmailValid: Boolean = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        val isPasswordValid: Boolean = _password.value.length >= 8
        _isEnabledLoginButton.value = isEmailValid && isPasswordValid
    }

    fun login() = viewModelScope.launch {
        _isLoading.value = true
       /* val isSuccessfulLogin: Boolean = loginRepository.login(_email.value, _password.value)
        if (isSuccessfulLogin) {
            _goToHome.value = true
        } else {
            _errorMessage.value = "No se pudo iniciar sesión"
        } */
        _isLoading.value = false
    }

    fun dismissError() = viewModelScope.launch {
        _errorMessage.value = ""
    }
}
