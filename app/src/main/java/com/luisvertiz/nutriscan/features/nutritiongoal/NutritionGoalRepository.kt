package com.luisvertiz.nutriscan.features.nutritiongoal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.model.NutritionGoalModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionGoalRepository @Inject constructor() {

    suspend fun saveNutritionGoal(nutritionGoal: NutritionGoalModel) {
        withContext(Dispatchers.IO) {
            try {
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                if (firebaseUser == null) {
                    throw Exception("Error al obtener los datos del usuario.")
                }

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.uid)
                    .update("nutritionGoal", nutritionGoal)
                    .await()

            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}
