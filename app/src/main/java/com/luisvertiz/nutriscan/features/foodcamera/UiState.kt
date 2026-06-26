package com.luisvertiz.nutriscan.features.foodcamera

import android.graphics.Bitmap

data class UiState(
    val takePhoto: Boolean = false,
    val capturedBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
)