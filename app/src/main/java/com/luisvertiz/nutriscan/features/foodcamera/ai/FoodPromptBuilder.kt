package com.luisvertiz.nutriscan.features.foodcamera.ai

object FoodPromptBuilder {


    fun buildFoodAnalysisPrompt(
        diabetesType: String,
    ): String = """
        Analiza esta imagen de comida para una persona con:
    
        Tipo de diabetes: $diabetesType
    
        Responde ÚNICAMENTE un JSON válido.
    
        {
            "foodName": "string",
            "calories": 0,
            "carbs": 0,
            "protein": 0,
            "fat": 0,
            "fiber": 0,
            "glycemicIndex": 0,
            "glycemicLoad": 0,
            "glycemicImpact": "BAJO|MEDIO|ALTO",
            "recommendation": "string"
        }
    
        Reglas:
    
        - No agregues markdown.
        - No uses ```json.
        - Devuelve solo JSON.
        - Considera que el usuario es diabético.
        - Estima las cantidades en gramos.
        - La recomendación debe enfocarse en control glucémico y con un lenguaje coloquial.
    """
}