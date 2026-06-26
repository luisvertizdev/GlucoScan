package com.luisvertiz.nutriscan.features.foodcamera.ai

import android.graphics.Bitmap
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.content
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FirebaseAiServiceImpl @Inject constructor(
    private val model: GenerativeModel,
) : FirebaseAiService {

    override suspend fun analyzeFood(
        bitmap: Bitmap,
        diabetesType: String,
    ): FoodAnalysisModel {

        val response = model.generateContent(
            content {
                image(bitmap)
                text(FoodPromptBuilder.buildFoodAnalysisPrompt(diabetesType))
            }
        )

        val json = response.text ?: error("Empty response")

        return Json.decodeFromString<FoodAnalysisModel>(
            json
        )
    }
}