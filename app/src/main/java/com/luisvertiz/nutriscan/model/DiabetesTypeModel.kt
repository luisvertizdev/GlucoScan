package com.luisvertiz.nutriscan.model

enum class DiabetesTypeModel(
    val description: String,
) {
    PRE_DIABETES(
        description = "Prediabetes",
    ),
    DIABETES_TYPE_1(
        description = "Diabetes Tipo 1",
    ),
    DIABETES_TYPE_2(
        description = "Diabetes Tipo 2",
    );
}