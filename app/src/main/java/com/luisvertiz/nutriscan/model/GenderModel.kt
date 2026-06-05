package com.luisvertiz.nutriscan.model

enum class GenderModel(
    val description: String,
    val nutritionFactor: Int,
) {
    MALE(
        description = "Masculino",
        nutritionFactor = 5
    ),
    FEMALE(
        description = "Femenino",
        nutritionFactor = -161
    );
}