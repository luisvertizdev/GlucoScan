package com.luisvertiz.nutriscan.features.history

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.luisvertiz.nutriscan.error.ErrorHandler.UnknownError
import com.luisvertiz.nutriscan.model.MealModel
import com.luisvertiz.nutriscan.util.FirestoreConstants.MEALS_COLLECTION
import com.luisvertiz.nutriscan.util.FirestoreConstants.USERS_COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor() {

    suspend fun getAllMeals(): List<MealModel> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser ?: throw UnknownError()
            val firebaseFirestore = FirebaseFirestore.getInstance()

            val querySnapshot = firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .collection(MEALS_COLLECTION)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            return@withContext querySnapshot.toObjects(MealModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            throw UnknownError()
        }
    }
}
