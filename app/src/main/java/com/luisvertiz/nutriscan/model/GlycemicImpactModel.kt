package com.luisvertiz.nutriscan.model

enum class GlycemicImpactModel(
    val description: String,
    val impactScore: Int,
) {
    LOW(
        description = "Bajo",
        impactScore = 1,
    ),
    MEDIUM(
        description = "Medio",
        impactScore = 2,
    ),
    HIGH(
        description = "Alto",
        impactScore = 3,
    );

    companion object {
        fun identify(description: String): GlycemicImpactModel? = GlycemicImpactModel.entries.firstOrNull { it.description == description }
    }
}