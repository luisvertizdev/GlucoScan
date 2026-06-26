package com.luisvertiz.nutriscan.features.profile

import androidx.annotation.StringRes
import com.luisvertiz.nutriscan.model.UserModel

data class UiState(
    val user: UserModel = UserModel(),
    val isScreenLoading: Boolean = false,
    val isLogoutButtonLoading: Boolean = false,
    @StringRes val idErrorMessage: Int? = null,
)