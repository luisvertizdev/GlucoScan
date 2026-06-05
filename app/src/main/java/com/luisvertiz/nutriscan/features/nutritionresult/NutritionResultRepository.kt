package com.luisvertiz.nutriscan.features.nutritionresult

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.luisvertiz.nutriscan.model.NutritionResultModel
import com.luisvertiz.nutriscan.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionResultRepository @Inject constructor() {

    suspend fun getNutritionResult(): NutritionResultModel {
        return withContext(Dispatchers.IO) {
            try {
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

                if (firebaseUser == null) {
                    throw Exception("Error al obtener los datos del usuario.")
                }

                val document = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()

                val userModel: UserModel? = document.toObject(UserModel::class.java)
                val nutritionResultModel: NutritionResultModel? = userModel?.nutritionGoal?.nutritionResult

                if (nutritionResultModel == null) {
                    throw Exception("Error al obtener el objetivo nutricional.")
                }
                return@withContext nutritionResultModel
            } catch (exception: Exception) {
                throw exception
            }
        }
    }
}
