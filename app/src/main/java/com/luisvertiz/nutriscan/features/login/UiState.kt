package com.luisvertiz.nutriscan.features.login

data class UiState(
    val email: String = "",
    val password: String = "",
    val isEnabledLoginButton: Boolean = false,
    val isLoading: Boolean = false,
    val idErrorMessage: Int? = null,
    val isPasswordVisible: Boolean = false,
)