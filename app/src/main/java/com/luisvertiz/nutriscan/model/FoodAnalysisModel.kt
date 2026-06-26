package com.luisvertiz.nutriscan.model

import kotlinx.serialization.Serializable

@Serializable
data class FoodAnalysisModel(
    val foodName: String = "",
    val calories: Int = 0,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val fiber: Int = 0,
    val glycemicIndex: Int = 0,
    val glycemicLoad: Int = 0,
    val glycemicImpact: String = "",
    val recommendation: String = "",
)