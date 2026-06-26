package com.luisvertiz.nutriscan.features.nutritiongoal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.NutritionGoalModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.NUTRITION_GOAL_FIELD
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionGoalRepository @Inject constructor() {

    suspend fun saveNutritionGoal(nutritionGoal: NutritionGoalModel) = withContext(Dispatchers.IO) {
        try {
            val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser ?: throw UnknownError()

            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .update(NUTRITION_GOAL_FIELD, nutritionGoal)
                .await()

        } catch (_: Exception) {
            throw UnknownError()
        }
    }
}