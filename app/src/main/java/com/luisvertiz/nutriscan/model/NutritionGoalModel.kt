package com.luisvertiz.nutriscan.model

data class NutritionGoalModel(
    val birthDate: String? = null,
    val gender: GenderModel? = null,
    val weightKg: Double? = null,
    val heightCm: Int? = null,
    val activityLevel: ActivityLevelModel? = null,
    val goalModel: GoalModel? = null,
    val nutritionResult: NutritionResultModel? = null,
)