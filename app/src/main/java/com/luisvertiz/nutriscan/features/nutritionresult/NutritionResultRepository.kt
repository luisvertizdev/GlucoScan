package com.luisvertiz.nutriscan.features.nutritionresult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionResultRepository @Inject constructor() {

    suspend fun getNutritionResult(): NutritionResultModel = withContext(Dispatchers.IO) {
        try {
            val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw UnknownError()

            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            val document: DocumentSnapshot = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userModel: UserModel = document.toObject(UserModel::class.java) ?: throw UnknownError()

            val nutritionResultModel: NutritionResultModel = userModel.nutritionGoal?.nutritionResult ?: throw UnknownError()
            return@withContext nutritionResultModel
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}
