package com.luisvertiz.nutriscan.features.foodresult

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.MEALS_COLLECTION
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class FoodResultRepository @Inject constructor() {

    suspend fun saveMeal(
        foodAnalysis: FoodAnalysisModel,
        imagePath: String
    ) = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser ?: throw UnknownError()
            
            val imageUrl = uploadImage(firebaseUser.uid, imagePath)
            
            val meal = MealModel(
                id = UUID.randomUUID().toString(),
                foodName = foodAnalysis.foodName,
                calories = foodAnalysis.calories,
                carbs = foodAnalysis.carbs,
                protein = foodAnalysis.protein,
                fat = foodAnalysis.fat,
                fiber = foodAnalysis.fiber,
                glycemicIndex = foodAnalysis.glycemicIndex,
                glycemicLoad = foodAnalysis.glycemicLoad,
                glycemicImpact = foodAnalysis.glycemicImpact,
                recommendation = foodAnalysis.recommendation,
                imageUrl = imageUrl,
                timestamp = System.currentTimeMillis()
            )

            val firebaseFirestore = FirebaseFirestore.getInstance()
            firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(MEALS_COLLECTION)
                .document(meal.id)
                .set(meal)
                .await()

        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError()
        }
    }

    private suspend fun uploadImage(userId: String, imagePath: String): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("meals/$userId/${UUID.randomUUID()}.jpg")
        val file = Uri.fromFile(File(imagePath))
        
        imageRef.putFile(file).await()
        return imageRef.downloadUrl.await().toString()
    }
}
