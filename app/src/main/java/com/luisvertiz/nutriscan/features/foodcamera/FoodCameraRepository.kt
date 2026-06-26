package com.luisvertiz.nutriscan.features.foodcamera

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.features.foodcamera.ai.FirebaseAiService
import com.luisvertiz.nutriscan.model.FoodAnalysisModel
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FoodCameraRepository @Inject constructor(
    private val firebaseAiService: FirebaseAiService,
) {

    suspend fun analyzeFood(
        bitmap: Bitmap,
    ): FoodAnalysisModel = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

            val firebaseUser: FirebaseUser = firebaseAuth.currentUser ?: throw UnknownError()

            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            val document = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user: UserModel = document.toObject(UserModel::class.java) ?: throw UnknownError()

            val foodAnalysisModel: FoodAnalysisModel = firebaseAiService.analyzeFood(
                bitmap = bitmap,
                diabetesType = user.nutritionGoal?.diabetesTypeModel?.description.orEmpty(),
            )

            return@withContext foodAnalysisModel
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}