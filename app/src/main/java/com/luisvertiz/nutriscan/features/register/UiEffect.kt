package com.luisvertiz.nutriscan.features.register

sealed class UiEffect {
    object GoBack : UiEffect()
    object GoToLogin : UiEffect()
}