package com.luisvertiz.nutriscan.model

enum class ActivityLevelModel(
    val description: String,
    val nutritionFactor: Double,
) {
    LOW(
        description = "Sedentario",
        nutritionFactor = 1.2
    ),
    MODERATE(
        description = "Moderado",
        nutritionFactor = 1.55
    ),
    HIGH(
        description = "Activo",
        nutritionFactor = 1.725
    );
}