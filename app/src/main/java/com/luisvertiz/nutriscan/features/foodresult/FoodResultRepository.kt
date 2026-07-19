package com.luisvertiz.nutriscan.features.foodresult

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.model.GlycemicImpactModel
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.MEALS_COLLECTION
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

class FoodResultRepository @Inject constructor() {

    suspend fun saveMeal(
        foodAnalysis: FoodAnalysisModel,
        imagePath: String
    ) = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()

            val firebaseUser = firebaseAuth.currentUser ?: throw UnknownError()

            val firebaseFirestore = FirebaseFirestore.getInstance()

            val mealRef = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(MEALS_COLLECTION)
                .document()

            val mealId = mealRef.id

            val imageUrl = uploadImage(
                userId = firebaseUser.uid,
                mealId = mealId,
                imagePath = imagePath
            )

            val meal = MealModel(
                id = Random.nextLong().toString(),
                foodName = foodAnalysis.foodName,
                calories = foodAnalysis.calories,
                carbs = foodAnalysis.carbs,
                protein = foodAnalysis.protein,
                fat = foodAnalysis.fat,
                fiber = foodAnalysis.fiber,
                glycemicImpact = GlycemicImpactModel.identify(foodAnalysis.glycemicImpact),
                imageUrl = imageUrl,
                timestamp = System.currentTimeMillis()
            )

            mealRef.set(meal).await()

        } catch (_: Exception) {
            throw UnknownError()
        }
    }

    private suspend fun uploadImage(
        userId: String,
        mealId: String,
        imagePath: String,
    ): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("meals/$userId/$mealId.jpg")
        val file = Uri.fromFile(File(imagePath))
        imageRef.putFile(file).await()
        return imageRef.downloadUrl.await().toString()
    }
}
