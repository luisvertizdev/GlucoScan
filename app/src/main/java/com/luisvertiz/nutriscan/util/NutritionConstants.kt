package com.luisvertiz.nutriscan.util

object NutritionConstants {
    const val WEIGHT_FACTOR: Double = 10.0
    const val HEIGHT_FACTOR: Double = 6.25
    const val AGE_FACTOR: Double = 5.0

    const val MALE_BMR_OFFSET: Int = 5
    const val FEMALE_BMR_OFFSET: Int = -161

    const val LOW_GLYCEMIC_IMPACT_MAX = 1.5
    const val MEDIUM_GLYCEMIC_IMPACT_MAX = 2.5

    const val LOW_IMPACT_PROGRESS: Float = 0.33f
    const val MEDIUM_IMPACT_PROGRESS: Float = 0.66f
    const val HIGH_IMPACT_PROGRESS: Float = 1.0f

    const val GLYCEMIC_IMPACT_MIN_LABEL = "0"
    const val GLYCEMIC_IMPACT_LOW_LABEL = "1.0"
    const val GLYCEMIC_IMPACT_MEDIUM_LABEL = "2.0"
    const val GLYCEMIC_IMPACT_HIGH_LABEL = "3.0"

}