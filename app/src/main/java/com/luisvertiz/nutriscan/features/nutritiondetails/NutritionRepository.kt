package com.luisvertiz.nutriscan.features.nutritiondetails

import android.text.format.DateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.model.UserModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.MEALS_COLLECTION
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionRepository @Inject constructor() {

    suspend fun getTodayMeals(): List<MealModel> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser ?: throw UnknownError()
            val firebaseFirestore = FirebaseFirestore.getInstance()

            val querySnapshot = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(MEALS_COLLECTION)
                .get()
                .await()

            val allMeals: List<MealModel> = querySnapshot.toObjects(MealModel::class.java)
            return@withContext allMeals.filter { DateUtils.isToday(it.timestamp) }
        } catch (_: Exception) {
            throw UnknownError()
        }
    }

    suspend fun getNutritionGoal(): NutritionResultModel = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser ?: throw UnknownError()
            val firebaseFirestore = FirebaseFirestore.getInstance()

            val document = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userModel: UserModel = document.toObject(UserModel::class.java) ?: throw UnknownError()
            return@withContext userModel.nutritionGoal?.nutritionResult ?: throw UnknownError()
        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}
