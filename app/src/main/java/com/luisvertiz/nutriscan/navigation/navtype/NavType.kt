package com.luisvertiz.nutriscan.navigation.navtype

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import kotlinx.serialization.json.Json

val FoodAnalysisNavType = object : NavType<FoodAnalysisModel>(
    isNullableAllowed = false,
) {
    override fun get(bundle: Bundle, key: String): FoodAnalysisModel? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): FoodAnalysisModel {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: FoodAnalysisModel): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: FoodAnalysisModel) {
        bundle.putString(key, Json.encodeToString(value))
    }
}