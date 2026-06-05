package com.luisvertiz.nutriscan.model

data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val nutritionGoal: NutritionGoalModel? = null
)