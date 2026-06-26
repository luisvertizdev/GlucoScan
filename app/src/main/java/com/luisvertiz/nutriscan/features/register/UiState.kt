package com.luisvertiz.nutriscan.features.register

data class UiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEnabledRegisterButton: Boolean = false,
    val isLoading: Boolean = false,
    val idSuccessRegisterMessage: Int? = null,
    val idErrorMessage: Int? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
)