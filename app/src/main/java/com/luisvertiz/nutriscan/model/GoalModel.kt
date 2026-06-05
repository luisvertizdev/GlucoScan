package com.luisvertiz.nutriscan.model

enum class GoalModel(
    val description: String,
    val nutritionFactor: Double,
    val proteinFactor: Double,
    val fatFactor: Double

) {
    LOSE_WEIGHT(
        description = "Bajar grasa",
        nutritionFactor = 0.85,
        proteinFactor = 2.2,
        fatFactor = 0.8
    ),
    MAINTAIN_WEIGHT(
        description = "Mantener peso",
        nutritionFactor = 1.0,
        proteinFactor = 1.8,
        fatFactor = 0.9
    ),
    GAIN_MUSCLE(
        description = "Ganar masa muscular",
        nutritionFactor = 1.10,
        proteinFactor = 2.0,
        fatFactor = 1.0
    );
}