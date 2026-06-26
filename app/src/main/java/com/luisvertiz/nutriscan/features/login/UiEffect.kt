package com.luisvertiz.nutriscan.features.login

sealed class UiEffect {
    object GoToRegister : UiEffect()
    object GoToDashboard : UiEffect()
}