package com.luisvertiz.nutriscan.model

data class MealModel(
    val id: String = "",
    val foodName: String = "",
    val calories: Int = 0,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val fiber: Int = 0,
    val glycemicImpact: GlycemicImpactModel? = null,
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
