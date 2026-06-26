package com.luisvertiz.nutriscan.features.foodcamera.ai

import android.graphics.Bitmap
import com.luisvertiz.nutriscan.model.FoodAnalysisModel

interface FirebaseAiService {

    suspend fun analyzeFood(
        bitmap: Bitmap,
        diabetesType: String,
    ): FoodAnalysisModel

}