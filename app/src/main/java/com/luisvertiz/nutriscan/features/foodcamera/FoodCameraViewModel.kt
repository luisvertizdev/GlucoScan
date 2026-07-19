package com.luisvertiz.nutriscan.features.foodcamera

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseApp
import com.luisvertiz.nutriscan.error.ErrorHandler
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodCameraViewModel @Inject constructor(
    private val foodCameraRepository: FoodCameraRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    fun capturePhoto() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                takePhoto = true
            )
        }
    }

    fun onPhotoCaptured(bitmap: Bitmap) = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                takePhoto = false,
                capturedBitmap = bitmap,
            )
        }
    }

    fun retakePhoto() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(
                takePhoto = false,
                capturedBitmap = null,
            )
        }
    }

    fun analyzeFood(bitmap: Bitmap) = viewModelScope.launch {
        try {
            _uiState.update { it.copy(isLoading = true) }
            println("projectId ${FirebaseApp.getInstance().options.projectId}")
            val foodAnalysisModel: FoodAnalysisModel = foodCameraRepository.analyzeFood(
                bitmap = bitmap,
            )
            val imagePath = FileUtil.saveBitmapToTempFile(getApplication(), bitmap)
            if (imagePath != null) {
                _uiEffect.emit(UiEffect.GoToFoodResult(foodAnalysisModel, imagePath))
            }
        } catch (_: Exception) {
            throw ErrorHandler.UnknownError()
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}