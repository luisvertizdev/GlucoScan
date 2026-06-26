package com.luisvertiz.nutriscan.model

import com.luisvertiz.nutriscan.util.NutritionConstants.FEMALE_BMR_OFFSET
import com.luisvertiz.nutriscan.util.NutritionConstants.MALE_BMR_OFFSET

enum class GenderModel(
    val description: String,
    val bmrOffset: Int,
) {
    MALE(
        description = "Masculino",
        bmrOffset = MALE_BMR_OFFSET,
    ),
    FEMALE(
        description = "Femenino",
        bmrOffset = FEMALE_BMR_OFFSET,
    );
}