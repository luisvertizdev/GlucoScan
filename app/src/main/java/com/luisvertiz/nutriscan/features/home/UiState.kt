package com.luisvertiz.nutriscan.features.home

import com.luisvertiz.nutriscan.model.UserModel

data class UiState(
    val user: UserModel = UserModel(),
    val totalConsumedCarbs: Int = 30,
    val todayDate: String = "",
    val isLoading: Boolean = false,
    val idErrorMessage: Int? = null,
)